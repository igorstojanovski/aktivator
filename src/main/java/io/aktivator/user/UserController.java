package io.aktivator.user;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.AuthUserDTO;
import io.aktivator.user.services.AuthorizationServiceException;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<AuthUserDTO> getUserInfo() throws AuthorizationServiceException {
        AuthUserDTO userInfoDTO = userService.getAuthUserInfo();
        return new ResponseEntity<>(userInfoDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AuthUserDTO> getUserInfoById(@PathVariable Long userId) throws AuthorizationServiceException {
        AuthUserDTO userInfoDTO = userService.getUserInfo(userId);
        return new ResponseEntity<>(userInfoDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> editUserInfo(@RequestBody AuthUserDTO authUserDTO) throws DataException {
        userService.updateUserInfo(authUserDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
