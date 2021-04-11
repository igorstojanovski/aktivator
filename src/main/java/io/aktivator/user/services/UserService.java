package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;
import io.aktivator.exceptions.DataException;
import io.aktivator.user.exceptions.UserNotRegisteredException;
import io.aktivator.user.model.User;
import io.aktivator.user.model.UserInformation;
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
        user.setUserInformation(new UserInformation());
        return userRepository.save(user);
    }

    public User registerUser() {
        return registerUser(getExternalUserId());
    }

    public AuthUserDTO getUserInformation() throws AuthorizationServiceException {
        AuthUserDTO dto = authClient.getUserByExternalId(getExternalUserId());
        User user = userRepository.findUserByExternalId(dto.getExternalId()).orElseThrow(() -> new DataException("No such user found."));
        dto.setLongAddress(user.getUserInformation().getLongAddress());
        dto.setExternalId(null);
        return dto;
    }

    public AuthUserDTO getUserInformation(String externalId) throws AuthorizationServiceException {
        return authClient.getUserByExternalId(externalId);
    }

    public void updateUserInfo(AuthUserDTO authUserDTO) {
        if(authUserDTO.getEmail().isEmpty() || authUserDTO.getName().isEmpty()) {
            throw new DataException("Missing mandatory User information.");
        }

        updateAuth0User(authUserDTO);
        updateInternalUserInformation(authUserDTO);
    }

    private void updateInternalUserInformation(AuthUserDTO authUserDTO) {
        User user = userRepository.findUserByExternalId(authUserDTO.getExternalId()).orElseThrow(() -> new DataException("No user with such external Id was found."));
        UserInformation userInformation = user.getUserInformation();
        if(userInformation == null) {
            userInformation = new UserInformation();
        }
        userInformation.setLongAddress(authUserDTO.getLongAddress());
        user.setUserInformation(userInformation);
        userRepository.save(user);
    }

    private void updateAuth0User(AuthUserDTO authUserDTO) {
        try {
            authClient.updateUserInfo(authUserDTO);
        } catch (Auth0Exception e) {
            throw new AuthorizationServiceException(e);
        }
    }

    public AuthUserDTO getUserInfo(Long userId) {
        String externalId = userRepository.findUserById(userId)
                .orElseThrow(() -> new DataException("No such user ID found.")).getExternalId();
        return getUserInformation(externalId);
    }
}
