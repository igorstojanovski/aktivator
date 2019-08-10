package io.aktivator.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
class ProfileController {

    private final ProfileService profileService;
    private ExternalUserService externalUserService;
    @Autowired
    ProfileController(ProfileService userService, ExternalUserService externalUserService) {
        this.profileService = userService;
        this.externalUserService = externalUserService;
    }

    @GetMapping("/public/{profileId}")
    ResponseEntity<Profile> getPublicProfile(@PathVariable String profileId) {
        Profile profile = profileService.getPublicProfile(profileId);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @GetMapping("/current")
    ResponseEntity<Profile> getCurrentUserInfo() {
        Profile profile = profileService.getProfile();
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PutMapping("/current")
    void editCurrentUserInfo(@RequestBody ProfileUpdateRequest profileUpdateRequest) {
        profileService.updateProfile(profileUpdateRequest);
    }
}
