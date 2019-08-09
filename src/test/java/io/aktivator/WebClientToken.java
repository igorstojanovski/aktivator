package io.aktivator;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WebClientToken {

    private final String realm;
    private final String client;
    private final String serverUrl;
    private final String secret;
    private AccessTokenResponse accessToken;
    private final Keycloak keycloak;

    @Autowired
    public WebClientToken() {

        Properties properties = new Properties();
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("application.properties")) {
            properties.load(in);
            System.out.println(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.realm = properties.getProperty("keycloak.realm");
        this.client = properties.getProperty("aktivator.keycloak.resource-web-test");
        this.serverUrl = properties.getProperty("keycloak.auth-server-url");
        this.secret = System.getenv("AKT_WEB_DEV_CLIENT_SECRET");

        keycloak = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .username(System.getenv("AKT_USER"))
            .password(System.getenv("AKT_PASSWORD"))
            .clientId(client)
            .clientSecret(secret)
            .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
            .build();

        accessToken = keycloak.tokenManager().getAccessToken();
    }

    public String getToken(String username, String password) {
        Keycloak local = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .clientId(client)
            .clientSecret(secret)
            .username(username)
            .password(password)
            .grantType(OAuth2Constants.PASSWORD)
            .build();

        return local.tokenManager().getAccessToken().getToken();
    }

    public String getValue() {
        return accessToken.getToken();
    }

    public void createUser(String username, String password, String email, String firstName, String lastName, String role) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setEmailVerified(true);
        user.setRequiredActions(Collections.emptyList());
        user.setAttributes(Collections.singletonMap("origin", Arrays.asList("demo")));

        // Get realm
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();

        // Create user (requires manage-users role)
        Response response = userResource.create(user);

        assertThat(response.getStatusInfo().getReasonPhrase()).isEqualTo("Created");
        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        RoleRepresentation testerRealmRole = realmResource.roles().get(role).toRepresentation();
        System.out.printf("User created with userId: %s%n", userId);

        userResource.get(userId).roles().realmLevel().add(Collections.singletonList(testerRealmRole));

        // Get client level role (requires view-clients role)
        RolesResource roles = realmResource.roles();
        RoleRepresentation userClientRole = roles.get("activist").toRepresentation();

        // Assign client level role to user
        userResource.get(userId).roles().realmLevel().add(Collections.singletonList(userClientRole));

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        passwordCred.setTemporary(false);

        // Set password credential
        userResource.get(userId).resetPassword(passwordCred);
    }

    public void removeUser(String username) {
        RealmResource realmResource = keycloak.realm(realm);
        UsersResource userResource = realmResource.users();
        List<UserRepresentation> result = userResource.search(username);

        UserRepresentation user = result.get(0);
        userResource.get(user.getId()).remove();
    }

}
