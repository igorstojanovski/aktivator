package io.aktivator.configuration.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.aktivator.model.UserDTO;
import io.aktivator.profile.ExternalUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserIdSerializerTest {

    @InjectMocks
    UserIdSerializer userIdSerializer = new UserIdSerializer();
    @Mock
    private JsonGenerator jsonGenerator;
    @Mock
    private SerializerProvider serializerProvider;
    @Mock
    private ExternalUserService externalUserService;
    @Captor
    private ArgumentCaptor<UserDTO> userDTOArgumentCaptor = ArgumentCaptor.forClass(UserDTO.class);

    @Test
    void shouldEmptyEmailAndAuthoritiesValues() throws IOException {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("bravo");
        userDTO.setEmail("johnny.bravo@gmail.com");
        userDTO.setAuthorities(Collections.singletonList("DUMMY"));
        when(externalUserService.getUser("123")).thenReturn(userDTO);

        userIdSerializer.serialize("123", jsonGenerator, serializerProvider);

        verify(jsonGenerator).writeObject(userDTOArgumentCaptor.capture());
        UserDTO captured = userDTOArgumentCaptor.getValue();
        assertThat(captured.getUsername()).isEqualTo("bravo");
        assertThat(captured.getEmail()).isEmpty();
        assertThat(captured.getAuthorities()).isEmpty();
    }
}
