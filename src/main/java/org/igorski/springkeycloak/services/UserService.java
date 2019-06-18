package org.igorski.springkeycloak.services;

import org.igorski.springkeycloak.model.UserDTO;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        KeycloakPrincipal principal = (KeycloakPrincipal) auth.getPrincipal();
        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();
        String username = accessToken.getPreferredUsername();
        String emailID = accessToken.getEmail();
        String lastName = accessToken.getFamilyName();
        String firstName = accessToken.getGivenName();
        accessToken.getId();

        UserDTO response = new UserDTO();
        response.setUsername(username);
        response.setId(principal.getName());
        response.setName(firstName);
        response.setSurname(lastName);
        response.setEmail(emailID);
        return response;
    }
}
