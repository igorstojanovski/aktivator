package io.aktivator.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
class ProfileController {

    private final ProfileService profileService;

    @Autowired
    ProfileController(ProfileService userService) {
        this.profileService = userService;
    }

    @GetMapping("/current")
    ResponseEntity<Profile> getCurrentUserInfo() {
        Profile profile = profileService.getProfile();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
