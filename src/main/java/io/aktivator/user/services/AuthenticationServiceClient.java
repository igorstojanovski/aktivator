package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;

public interface AuthenticationServiceClient {
    AuthUserDTO getUserByExternalId(String externalId) throws AuthorizationServiceException;
    void updateUserInfo(AuthUserDTO authUserDTO, String externalId) throws AuthorizationServiceException, Auth0Exception;
}
