package org.igorski.springkeycloak.services;

import org.igorski.springkeycloak.model.DataException;
import org.igorski.springkeycloak.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;
    @Mock
    private KeycloakClient kecloakClient;
    @Mock
    private Keycloak keycloak;
    @Mock
    private RealmResource realmResource;
    @Mock
    private UsersResource usersResource;
    private List<UserRepresentation> userRepresentations;
    @Mock
    private UserRepresentation userRepresentation;

    @BeforeEach
    public void beforeEach() {
        userRepresentations = new ArrayList<>();

        userService = new UserService(kecloakClient);
        when(kecloakClient.getKeycloak()).thenReturn(keycloak);
        when(keycloak.realm("aktivator-test")).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.search("223")).thenReturn(userRepresentations);
    }

    @Test
    public void shouldGetUserById() throws DataException {
        userRepresentations.add(userRepresentation);

        when(userRepresentation.getUsername()).thenReturn("igorce");
        when(userRepresentation.getFirstName()).thenReturn("Igor");
        when(userRepresentation.getLastName()).thenReturn("Stojanovski");
        when(userRepresentation.getEmail()).thenReturn("igorce@gmail.com");

        UserDTO userDTO = userService.getUser("223");
        assertThat(userDTO.getUsername()).isEqualTo("igorce");
        assertThat(userDTO.getName()).isEqualTo("Igor");
        assertThat(userDTO.getSurname()).isEqualTo("Stojanovski");
        assertThat(userDTO.getEmail()).isEqualTo("igorce@gmail.com");
    }

    @Test
    public void shouldThrowExceptionWhenNoUserWasFound() {
        when(usersResource.search("223")).thenReturn(Collections.emptyList());
        assertThrows(DataException.class, () -> userService.getUser("223"));
    }
}
