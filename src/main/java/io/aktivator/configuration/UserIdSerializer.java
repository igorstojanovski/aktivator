package io.aktivator.configuration;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.aktivator.user.services.UserDto;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdSerializer extends StdConverter<Long, UserDto> {
    @Autowired
    private UserService userService;

    @Override
    public UserDto convert(Long value) {
        return userService.getInformationInternal(value);
    }
}
