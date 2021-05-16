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

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> registerUser() {
        return new ResponseEntity<>(userService.registerUser(userService.getExternalUserId()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<AuthUserDTO> getUserInfo() throws AuthorizationServiceException {
        AuthUserDTO userInfoDTO = userService.getInformationExternal(userService.getExternalUserId());
        return new ResponseEntity<>(userInfoDTO, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AuthUserDTO> getUserInfoById(@PathVariable Long userId) throws AuthorizationServiceException {
        AuthUserDTO userInfoDTO = userService.getInformationInternal(userId);
        return new ResponseEntity<>(userInfoDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> editUserInfo(@RequestBody AuthUserDTO authUserDTO) throws DataException {
        userService.updateUserInfo(authUserDTO, userService.getExternalUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
