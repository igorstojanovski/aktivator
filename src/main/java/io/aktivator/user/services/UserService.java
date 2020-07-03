package io.aktivator.user.services;

import io.aktivator.user.exceptions.UserNotRegisteredException;
import io.aktivator.user.model.User;
import io.aktivator.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        return getUser(getExternalUserId())
                .orElseThrow(() -> new UserNotRegisteredException("User with such external ID is not registered."));
    }

    public String getExternalUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null || auth.getPrincipal() == null) {
            throw new UserNotRegisteredException("Not authorized.");
        }
        Jwt jwt = (Jwt) auth.getPrincipal();
        return (String) jwt.getClaims().get("sub");
    }

    public Optional<User> getUser(String externalUserId) {
        return userRepository.findUserByExternalId(externalUserId);
    }

    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    public User registerUser() {
        User user = new User();
        user.setExternalId(getExternalUserId());
        return userRepository.save(user);
    }
}
