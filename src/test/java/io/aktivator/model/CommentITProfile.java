package io.aktivator.model;

import io.aktivator.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
public class CommentITProfile {

    @Bean
    public UserService getUserServiceMock() throws DataException {
        UserService userService = mock(UserService.class);
        UserDTO userDTO = new UserDTO();
        userDTO.setSurname("Stojanovski");
        userDTO.setName("Igor");
        userDTO.setEmail("igorce@gmail.com");
        userDTO.setId("223");
        when(userService.getUser("223")).thenReturn(userDTO);

        return userService;
    }
}
