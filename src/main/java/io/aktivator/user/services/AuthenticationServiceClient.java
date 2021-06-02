package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;

public interface AuthenticationServiceClient {
    UserDto getUserByExternalId(String externalId) throws AuthorizationServiceException;
    void updateUserInfo(UserDto userDto, String externalId) throws AuthorizationServiceException, Auth0Exception;
}
