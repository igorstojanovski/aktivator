package io.aktivator.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aktivator.extensions.KeycloakExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, KeycloakExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRunAsTest(@TestParameter("admin_token") String token) throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/profile/current")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

        Profile profile = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), Profile.class);
        assertThat(profile.getName()).isEqualTo("John");
        assertThat(profile.getSurname()).isEqualTo("Doe");
    }
}
