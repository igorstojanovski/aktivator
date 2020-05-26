package io.aktivator.user;

import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> registerUser() {
        String externalUserId = userService.getExternalUserId();
        Optional<User> optionalUser = userService.getUser(externalUserId);
        User user;
        if(optionalUser.isEmpty()) {
            user = userService.registerUser();
        } else {
            user = optionalUser.get();
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
