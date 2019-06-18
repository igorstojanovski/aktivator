package org.igorski.springkeycloak;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class WebClientToken {

    private AccessTokenResponse accessToken;

    @Autowired
    public WebClientToken(@Value("${keycloak.auth-server-url}") String serverUrl,
                          @Value("${keycloak.realm}") String realm,
                          @Value("${aktivator.keycloak.resource-web-test}") String client,
                          @Value("${aktivator.keycloak.credentials.secret-web-test}") String secret,
                          @Value("${aktivator.keycloak.user}") String keycloakUser,
                          @Value("${aktivator.keycloak.password}") String keycloakPassword) {
        Keycloak keycloak = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .username(keycloakUser)
            .password(keycloakPassword)
            .clientId(client)
            .clientSecret(secret)
            .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(20).build())
            .build();

        accessToken = keycloak.tokenManager().getAccessToken();
    }

    public String getValue() {
        return accessToken.getToken();
    }
}
