package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;
import io.aktivator.exceptions.DataException;
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
    private final AuthenticationServiceClient authClient;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationServiceClient authClient) {
        this.authClient = authClient;
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

    public User registerUser(String externalUserId) {
        User user = new User();
        user.setExternalId(externalUserId);
        return userRepository.save(user);
    }

    public User registerUser() {
        return registerUser(getExternalUserId());
    }

    public AuthUserDTO getAuthUserInfo() throws AutherizationServiceException {
        return authClient.getUserByExternalId(getExternalUserId());
    }

    public AuthUserDTO getAuthUserInfo(String externalId) throws AutherizationServiceException {
        return authClient.getUserByExternalId(externalId);
    }

    public void updateUserInfo(AuthUserDTO authUserDTO) {
        if(authUserDTO.getEmail().isEmpty() || authUserDTO.getName().isEmpty()) {
            throw new DataException("Email and name cannot be empty.");
        }

        try {
            authClient.updateUserInfo(authUserDTO, getExternalUserId());
        } catch (AutherizationServiceException | Auth0Exception e) {
            throw new DataException(e);
        }
    }
}
