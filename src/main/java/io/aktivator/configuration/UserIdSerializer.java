package io.aktivator.configuration;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.aktivator.user.services.AuthUserDTO;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdSerializer extends StdConverter<Long, AuthUserDTO> {
    @Autowired
    private UserService userService;

    @Override
    public AuthUserDTO convert(Long value) {
        return userService.getInformationInternal(value);
    }
}
