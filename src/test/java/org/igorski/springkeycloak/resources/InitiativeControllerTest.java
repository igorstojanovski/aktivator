package org.igorski.springkeycloak.resources;

import org.igorski.springkeycloak.model.Initiative;
import org.igorski.springkeycloak.model.UserDTO;
import org.igorski.springkeycloak.services.InitiativeService;
import org.igorski.springkeycloak.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InitiativeControllerTest {

    private InitiativeController initiativeController;
    @Mock
    private InitiativeService initiativeService;
    @Mock
    UserService userService;

    @BeforeEach
    public void before() {
        initiativeController = new InitiativeController(initiativeService, userService);
    }

    @Test
    public void shouldReturnCorrectInitiative() {
        Initiative initiative = new Initiative();
        initiative.setName("Test Initiative");
        initiative.setDescription("Test Description");
        initiative.setUserId("a");

        Initiative initiativeWithId = new Initiative();
        initiativeWithId.setName("Test Initiative");
        initiativeWithId.setDescription("Test Description");
        initiativeWithId.setUserId("a");
        initiativeWithId.setId(1L);

        when(initiativeService.createInitiative(initiative)).thenReturn(initiativeWithId);
        UserDTO user = new UserDTO();
        user.setId("a");
        when(userService.getCurrentUser()).thenReturn(user);

        ResponseEntity<Initiative> response = initiativeController.createInitiative(initiative);
        Initiative body = response.getBody();

        assertThat(body).isNotNull();
        assertThat(body.getUserId()).isEqualTo("a");
        assertThat(body.getId()).isEqualTo(1L);
    }
}
