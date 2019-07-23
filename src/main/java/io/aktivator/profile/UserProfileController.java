package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
            return new ResponseEntity<>(profileService.getProfile(user.getId()), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping
    public ResponseEntity<UserProfile> updateUserProfile(@RequestBody UserProfileUpdateRequest request) {
        UserDTO user = userService.getCurrentUser();
        try {
            return new ResponseEntity<>(profileService.getProfile(user.getId()), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
