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

import java.util.*;

import static io.aktivator.user.services.UserCacheEntry.isOlderThanADay;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final AuthenticationServiceClient authClient;
  private static final Map<String, UserCacheEntry> USER_CACHE = new HashMap<>();

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

  private User registerExternalUser(String externalUserId, UserDto userDto) {
    User user = new User();
    user.setExternalId(externalUserId);
    UserInformation userInformation = new UserInformation();
    userInformation.setName(userDto.getName());
    userInformation.setSurname(userDto.getSurname());
    userInformation.setLongAddress(userInformation.getLongAddress());
    userInformation.setUsername(userDto.getNickname());
    user.setUserInformation(userInformation);
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
    UserCacheEntry userCacheEntry = USER_CACHE.get(externalUserId);
    UserDto authUserDto;
    if (userCacheEntry == null || isOlderThanADay(userCacheEntry.timestamp)) {
      authUserDto = authClient.getUserByExternalId(externalUserId);
      cacheUserDto(externalUserId, authClient.getUserByExternalId(externalUserId));
    } else {
      authUserDto = userCacheEntry.userDto;
    }
    return new UserDto(
        user.getUserInformation().getName(),
        user.getUserInformation().getSurname(),
        authUserDto.getEmail(),
        user.getUserInformation().getUsername(),
        authUserDto.getPhotoUrl(),
        authUserDto.getMetadata(),
        user.getUserInformation().getLongAddress());
  }

  private void cacheUserDto(String externalUserId, UserDto authUserDto) {
    USER_CACHE.put(externalUserId, new UserCacheEntry(authUserDto, new Date()));
  }

  private UserDto getCachedUserDto(String externalUserId) {
    return USER_CACHE.get(externalUserId).userDto;
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

  public User registerUser(String externalUserId, UserDto userDto) {
    Optional<User> optionalUser = getUser(externalUserId);
    User user;
    if (optionalUser.isEmpty()) {
      user = registerExternalUser(externalUserId, userDto);
    } else {
      throw new ResourceAlreadyExists("This user is already registered.");
    }
    return user;
  }
}
