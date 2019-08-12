package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.profile.requests.ProfileUpdateRequest;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeycloakService implements ExternalUserService {

    private final String realm;
    private final Keycloak keycloak;

    public KeycloakService(@Value("${keycloak.auth-server-url}") String serverUrl,
                           @Value("${keycloak.realm}") String realm,
                           @Value("${aktivator.keycloak.resource-web-test}") String client,
                           @Value("${aktivator.keycloak.credentials.secret-web-test}") String secret,
                           @Value("${aktivator.keycloak.user}") String keycloakUser,
                           @Value("${aktivator.keycloak.password}") String keycloakPassword) {
        this.realm = realm;
        keycloak = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .username(keycloakUser)
            .password(keycloakPassword)
            .clientId(client)
            .clientSecret(secret)
            .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
            .build();
    }

    @Override
    public void editUser(ProfileUpdateRequest profileUpdateRequest, String ownerId) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();

        UserResource user = userResource.get(ownerId);
        UserRepresentation representation = user.toRepresentation();
        if (profileUpdateRequest.getFirstName() != null && !profileUpdateRequest.getFirstName().isEmpty()) {
            representation.setFirstName(profileUpdateRequest.getFirstName());
        }
        if (profileUpdateRequest.getLastName() != null && !profileUpdateRequest.getLastName().isEmpty()) {
            representation.setLastName(profileUpdateRequest.getLastName());
        }
        user.update(representation);
    }

    @Override
    public UserDTO getUser(String ownerId) {
        UserDTO userDTO = new UserDTO();
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();

        UserRepresentation representation = userResource.get(ownerId).toRepresentation();
        userDTO.setName(representation.getFirstName());
        userDTO.setSurname(representation.getLastName());
        userDTO.setUsername(representation.getUsername());
        userDTO.setEmail(representation.getEmail());
        userDTO.setId(ownerId);
        return userDTO;
    }

}
