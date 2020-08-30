package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthorizationServiceException extends RuntimeException {
    public AuthorizationServiceException(String message) {
        super(message);
    }

    public AuthorizationServiceException(Auth0Exception e) {
        super(e);
    }
}
