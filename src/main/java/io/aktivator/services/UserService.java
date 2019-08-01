package io.aktivator.services;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final KeycloakClient keycloakClient;

    @Autowired
    public UserService(KeycloakClient keycloakClient) {
        this.keycloakClient = keycloakClient;
    }

    public UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        KeycloakPrincipal principal = (KeycloakPrincipal) auth.getPrincipal();


        List<String> authorities = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(String::toUpperCase)
            .map(s -> s.split("_")[1])
            .collect(Collectors.toList());

        return getUserDto(principal, authorities);
    }

    public UserDTO getUserDto(KeycloakPrincipal principal, List<String> authorities) {
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        String emailID = accessToken.getEmail();
        String lastName = accessToken.getFamilyName();
        String firstName = accessToken.getGivenName();
        accessToken.getId();

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setId(principal.getName());
        userDTO.setName(firstName);
        userDTO.setSurname(lastName);
        userDTO.setEmail(emailID);
        userDTO.setAuthorities(authorities);
        return userDTO;
    }

    public UserDTO getUser(String userId) throws DataException {

        Keycloak keycloak = keycloakClient.getKeycloak();
        RealmResource realm = keycloak.realm("aktivator-test");
        List<UserRepresentation> users = realm.users().search(userId);

        if(users.isEmpty()) {
            throw new DataException("No such user found: " + userId);
        }

        UserRepresentation user = users.get(0);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setEmail(user.getEmail());
        userDTO.setName(user.getFirstName());
        userDTO.setSurname(user.getLastName());
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }
}
