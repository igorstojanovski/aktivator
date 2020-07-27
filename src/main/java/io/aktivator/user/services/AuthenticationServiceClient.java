package io.aktivator.user.services;

public interface AuthenticationServiceClient {
    AuthUserDTO getUserByExternalId(String externalId) throws AutherizationServiceException;
}
