package org.igorski.springkeycloak.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.igorski.springkeycloak.model.Initiative;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${keycloak.auth-server-url}")
    private String authUrl;


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldCreateInitiative() throws Exception {
        Keycloak keycloak = KeycloakBuilder.builder()
                .build();

        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();

        Initiative initiative = new Initiative();
        initiative.setDescription("New initiative to collect money for medical bills.");
        initiative.setName("Let us help Ben!");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/initiative")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken.getToken())
                .content(OBJECT_MAPPER.writeValueAsString(initiative)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        initiative = OBJECT_MAPPER.readValue(result.getResponse().getContentAsString(), Initiative.class);

        assertThat(initiative.getId()).isNotNull();
        assertThat(initiative.getUserId()).isNotNull();
    }
}