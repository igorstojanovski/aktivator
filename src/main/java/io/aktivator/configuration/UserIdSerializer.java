package io.aktivator.configuration;

import com.fasterxml.jackson.databind.util.StdConverter;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserIdSerializer extends StdConverter<Long, String> {
    @Autowired
    private UserService externalUserService;

    @Override
    public String convert(Long value) {
        User user = externalUserService.getCurrentUser();
        return user.getExternalId();
    }
}
