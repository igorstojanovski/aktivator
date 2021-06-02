package io.aktivator.user.services;

import io.aktivator.exceptions.DataException;
import io.aktivator.exceptions.ResourceAlreadyExists;
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
        .orElseThrow(
            () -> new UserNotRegisteredException("User with such external ID is not registered."));
  }

  public String getExternalUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || auth.getPrincipal() == null) {
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

  private User registerExternalUser(String externalUserId) {
    User user = new User();
    user.setExternalId(externalUserId);
    user.setUserInformation(new UserInformation());
    return userRepository.save(user);
  }

  public UserDto getInformationExternal(String externalUserId)
      throws AuthorizationServiceException {
    User user =
        userRepository
            .findUserByExternalId(externalUserId)
            .orElseThrow(() -> new DataException("No such user found."));
    return combineUserInformation(externalUserId, user);
  }

  private UserDto combineUserInformation(String externalUserId, User user) {
    UserDto authUserDto = authClient.getUserByExternalId(externalUserId);
    return new UserDto(
        user.getUserInformation().getName(),
        user.getUserInformation().getSurname(),
        authUserDto.getEmail(),
        user.getUserInformation().getUsername(),
        authUserDto.getPhotoUrl(),
        authUserDto.getMetadata(),
        user.getUserInformation().getLongAddress());
  }

  public void updateUserInfo(UserDto userDto, String externalId) {
    updateInternalUserInformation(userDto, externalId);
  }

  private void updateInternalUserInformation(UserDto userDto, String externalId) {
    User user =
        userRepository
            .findUserByExternalId(externalId)
            .orElseThrow(() -> new DataException("No user with such external Id was found."));
    UserInformation userInformation = user.getUserInformation();
    if (userInformation == null) {
      userInformation = new UserInformation();
    }
    userInformation.setLongAddress(userDto.getLongAddress());
    userInformation.setName(userDto.getName());
    userInformation.setSurname(userDto.getSurname());
    user.setUserInformation(userInformation);
    userRepository.save(user);
  }

  public UserDto getInformationInternal(Long userId) {
    User user =
        userRepository
            .findUserById(userId)
            .orElseThrow(() -> new DataException("No such user ID found: " + userId));
    return combineUserInformation(user.getExternalId(), user);
  }

  public User registerUser(String externalUserId) {
    Optional<User> optionalUser = getUser(externalUserId);
    User user;
    if (optionalUser.isEmpty()) {
      user = registerExternalUser(externalUserId);
    } else {
      throw new ResourceAlreadyExists("This user is already registered.");
    }
    return user;
  }
}
