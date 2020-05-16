package io.aktivator.configuration.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.aktivator.profile.ExternalUserService;
import io.aktivator.user.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
public class UserIdSerializer extends JsonSerializer<String> {
    @Autowired
    private ExternalUserService externalUserService;

    @Override
    public void serialize(String ownerId, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        UserDTO user = externalUserService.getUser(ownerId);
        user.setEmail("");
        user.setAuthorities(Collections.emptyList());
        jsonGenerator.writeObject(user);
    }
}
