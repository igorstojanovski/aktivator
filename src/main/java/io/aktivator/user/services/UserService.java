package io.aktivator.user.services;

import io.aktivator.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User getCurrentUser() {
        return new User();
    }

    public String getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) auth.getPrincipal();
        return (String) jwt.getClaims().get("sub");
    }
}
