package io.aktivator.campaign.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.aktivator.extensions.KeycloakExtension;
import io.aktivator.profile.TestParameter;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class, KeycloakExtension.class})
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateComment(@TestParameter("admin_token") String token) throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .get("/api/profile/current")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    }
}
