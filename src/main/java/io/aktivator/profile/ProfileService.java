package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ProfileService {

    private final UserService userService;

    @Autowired
    ProfileService(UserService userService) {
        this.userService = userService;
    }

    Profile getProfile() {
        UserDTO current = userService.getCurrentUser();
        Profile profile = new Profile();
        profile.setEmail(current.getEmail());
        profile.setName(current.getName());
        profile.setSurname(current.getSurname());
        profile.setUsername(current.getUsername());

        return profile;
    }
}
