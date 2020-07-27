package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;

public class AutherizationServiceException extends Exception {
    public AutherizationServiceException(String message) {
        super(message);
    }

    public AutherizationServiceException(Auth0Exception e) {
        super(e);
    }
}
