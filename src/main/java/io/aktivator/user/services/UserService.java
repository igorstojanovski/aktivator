package io.aktivator.user.services;

import io.aktivator.user.model.UserDTO;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String getUserId() {
        return "";
    }

    public UserDTO getCurrentUser() {
        return new UserDTO();
    }
}
