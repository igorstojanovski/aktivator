package io.aktivator.user.services;

import com.auth0.client.auth.AuthAPI;
import com.auth0.client.mgmt.ManagementAPI;
import com.auth0.client.mgmt.filter.UserFilter;
import com.auth0.exception.Auth0Exception;
import com.auth0.json.auth.UserInfo;
import com.auth0.json.mgmt.users.User;
import com.auth0.json.mgmt.users.UsersPage;
import com.auth0.net.Request;
import io.aktivator.exceptions.BadConfigurationException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class Auth0Client implements AuthenticationServiceClient {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final AuthAPI authApi;

    private final ManagementAPI managementAPI;

    public Auth0Client(@Value("${AUTH0_CLIENT_ID}") String auth0ClientId,
                       @Value("${AUTH0_CLIENT_SECRET}") String auth0ClientSecret,
                       @Value("${AUTH0_DOMAIN}") String auth0Domain) throws IOException, InterruptedException {

        if(auth0ClientId == null || auth0ClientSecret == null || auth0Domain == null) {
            throw new BadConfigurationException();
        }

        authApi = new AuthAPI(auth0Domain, auth0ClientId, auth0ClientSecret);
        HttpClient httpClient = HttpClient.newHttpClient();

        String auth0TokenUrl = "https://" + auth0Domain + "/oauth/token";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(auth0TokenUrl))
                .timeout(Duration.ofMinutes(1))
                .POST(HttpRequest.BodyPublishers.ofString("{\"client_id\":\"" + auth0ClientId + "\"," +
                        "\"client_secret\":\"" + auth0ClientSecret + "\"," +
                        "\"audience\":\"https://" + auth0Domain + "/api/v2/\"," +
                        "\"grant_type\":\"client_credentials\"}"))

                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> tokenJsonResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String json = tokenJsonResponse.body();

        TokenResponse tokenResponse = OBJECT_MAPPER.readValue(json, TokenResponse.class);
        if(tokenResponse != null && tokenResponse.getError() != null) {
            throw new AuthorizationServiceException(tokenResponse.getError() + " : " + tokenResponse.getError_description());
        }
        managementAPI = new ManagementAPI(auth0Domain, tokenResponse.getAccess_token());
    }

    private void getUserInfo(String accessToken) throws Auth0Exception {
        Request<UserInfo> request = authApi.userInfo(accessToken);
        UserInfo userInfo = request.execute();
        Map<String, Object> values = userInfo.getValues();

        for( Map.Entry<String, Object> entry : values.entrySet()) {
            System.out.println("Key: " + entry.getKey());
            System.out.println("Value: " + entry.getValue());
        }
    }

    private void getAllUsers() throws Auth0Exception {
        UserFilter userFilter = new UserFilter();
        UsersPage result = managementAPI.users().list(userFilter).execute();
        List<User> items = result.getItems();
        for(User user : items) {
            System.out.println(user.getEmail());
        }
    }

    private List<AuthUserDTO> getUserByQuery(String query) throws Auth0Exception {
        System.out.println("Querying authentication service: " + query);
        List<User> items = getUsers(query);
        List<AuthUserDTO> authUsers = new ArrayList<>();

        for(User user : items) {

            AuthUserDTO authUserDTO = new AuthUserDTO();
            authUserDTO.setEmail(user.getEmail());
            authUserDTO.setName(user.getName());
            authUserDTO.setSurname(user.getFamilyName());
            authUserDTO.setNickname(user.getNickname());
            authUserDTO.setPhotoUrl(user.getPicture());
            authUserDTO.setMetadata(user.getUserMetadata());

            authUsers.add(authUserDTO);
        }

        return authUsers;
    }

    private List<User> getUsers(String query) throws Auth0Exception {
        UserFilter userFilter = new UserFilter();
        userFilter.withQuery(query);
        UsersPage result = managementAPI.users().list(userFilter).execute();
        return result.getItems();
    }

    @Override
    public AuthUserDTO getUserByExternalId(String externalId) throws AuthorizationServiceException {
        AuthUserDTO authUserDTO;
        try {
            List<AuthUserDTO> users = getUserByQuery("user_id:" + externalId);
            if(users.size() != 1) {
                throw new AuthorizationServiceException("Got wrong user response from authentication service.");
            }
            authUserDTO = users.get(0);
        } catch (Auth0Exception e) {
            throw new AuthorizationServiceException(e);
        }

        return authUserDTO;
    }

    @Override
    public void updateUserInfo(AuthUserDTO authUserDTO, String externalUserId) throws AuthorizationServiceException, Auth0Exception {

        User userToEdit = getUserById(externalUserId);

        userToEdit.setEmail(authUserDTO.getEmail());
        userToEdit.setName(authUserDTO.getName());
        userToEdit.setFamilyName(authUserDTO.getSurname());
        userToEdit.setNickname(authUserDTO.getNickname());
        userToEdit.setPicture(authUserDTO.getPhotoUrl());
        userToEdit.setUserMetadata(authUserDTO.getMetadata());
        managementAPI.users().update(externalUserId, userToEdit);
    }

    private User getUserById(String externalUserId) throws Auth0Exception, AuthorizationServiceException {
        List<User> users = getUsers("user_id:" + externalUserId);
        if(users.size() != 1) {
            throw new AuthorizationServiceException("Got wrong user response from authentication service.");
        }

        return users.get(0);
    }
}
