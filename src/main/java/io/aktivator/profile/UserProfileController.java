package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user_profile")
public class UserProfileController {

    private final UserService userService;
    private final ProfileService profileService;

    @Autowired
    public UserProfileController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @GetMapping
    @RequestMapping("/{userId}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(profileService.getProfile(userId), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<UserProfile> getCurrentProfile() {
        UserDTO user = userService.getCurrentUser();
        try {
            UserProfile profile = profileService.getProfile(user.getId());
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping
    ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfileUpdateRequest request) {
        UserDTO user = userService.getCurrentUser();
        try {
            UserProfile profile = profileService.getProfile(user.getId());
            changeSurname(request, profile);
            changeName(request, profile);
            changeUsername(request, profile);
            return new ResponseEntity<>(profileService.updateProfile(profile), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private void changeUsername(@RequestBody UserProfileUpdateRequest request, UserProfile profile) {
        String username = request.getUsername();
        if (username != null && !username.isEmpty()) {
            profile.setUsername(username);
        }
    }

    private void changeName(@RequestBody UserProfileUpdateRequest request, UserProfile profile) {
        String name = request.getName();
        if (name != null && !name.isEmpty()) {
            profile.setName(name);
        }
    }

    private void changeSurname(@RequestBody UserProfileUpdateRequest request, UserProfile profile) {
        String surname = request.getSurname();
        if (surname != null && !surname.isEmpty()) {
            profile.setSurname(surname);
        }
    }

    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile() {
        UserDTO user = userService.getCurrentUser();
        try {
            return new ResponseEntity<>(profileService.createProfile(user), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
