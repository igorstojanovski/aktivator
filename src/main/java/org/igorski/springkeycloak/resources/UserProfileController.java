package org.igorski.springkeycloak.resources;

import org.igorski.springkeycloak.model.DataException;
import org.igorski.springkeycloak.model.UserDTO;
import org.igorski.springkeycloak.model.UserProfile;
import org.igorski.springkeycloak.services.ProfileService;
import org.igorski.springkeycloak.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
