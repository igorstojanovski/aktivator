package io.aktivator.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aktivator.WebClientToken;
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

import static io.aktivator.TestRunConfiguration.DUMMY_PASS;
import static io.aktivator.TestRunConfiguration.SIMPLE_USER;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private WebClientToken webClientToken;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRunAsTest() throws Exception {
        System.out.println(webClientToken.getValue());
        String token = webClientToken.getToken(SIMPLE_USER, DUMMY_PASS);

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
