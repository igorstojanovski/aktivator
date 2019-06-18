package org.igorski.springkeycloak.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.igorski.springkeycloak.WebClientToken;
import org.igorski.springkeycloak.model.Initiative;
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
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InitiativeControllerIT {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebClientToken webClientToken;

    @Test
    public void shouldCreateInitiative() throws Exception {

        Initiative initiative = new Initiative();
        initiative.setDescription("New initiative to collect money for medical bills.");
        initiative.setName("Let us help Ben!");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/initiative")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + webClientToken.getValue())
                .content(OBJECT_MAPPER.writeValueAsString(initiative)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        initiative = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), Initiative.class);

        assertThat(initiative.getId()).isNotNull();
        assertThat(initiative.getUserId()).isNotNull();
    }
}
