package io.aktivator.user.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class UserDto {
    private final String name;
    private final String surname;
    private final String email;
    private final String nickname;
    private final String photoUrl;
    private final Map<String, Object> metadata;
    private final String longAddress;
}
