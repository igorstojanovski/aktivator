package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;

public interface AuthenticationServiceClient {
    AuthUserDTO getUserByExternalId(String externalId) throws AutherizationServiceException;
    void updateUserInfo(AuthUserDTO authUserDTO, String externalUserId) throws AutherizationServiceException, Auth0Exception;
}
