package io.aktivator.user.services;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Date;

@AllArgsConstructor
public class UserCacheEntry {
    UserDto userDto;
    Date timestamp;

    public static boolean isOlderThanADay(Date timestamp) {
      return Duration.between(timestamp.toInstant(), new Date().toInstant())
              .compareTo(Duration.ofHours(24)) > 0;
    }
}
