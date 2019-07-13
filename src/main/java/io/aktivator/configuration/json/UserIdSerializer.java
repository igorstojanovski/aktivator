package io.aktivator.configuration.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.aktivator.model.UserDTO;
import io.aktivator.model.DataException;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserIdSerializer extends JsonSerializer<String> {

    @Autowired
    private UserService userService;

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        UserDTO user;
        try {
            user = userService.getUser(s);
            jsonGenerator.writeObject(user);
        } catch (DataException e) {
            jsonGenerator.writeObject("");
        }
    }
}
