package io.aktivator.user;

import io.aktivator.user.services.UserDto;

import java.util.HashMap;
import java.util.Map;

public class UserTestHelper {
  public static final String EXTERNAL_USER_ID = "12user";
  public static final String DUMMY_EMAIL_COM = "dummy@email.com";
  public static final String USER_ADDRESS = "A very long address.";
  public static final String SHERLOCK = "Sherlock";
  public static final String HOLMES = "Holmes";

  public UserDto getAuthUserDTO() {
    Map<String, Object> metadata = new HashMap<>();
    metadata.put("keyOne", "valueOne");
    return new UserDto(SHERLOCK, HOLMES, DUMMY_EMAIL_COM, "causea", "", metadata, "");
  }
}
