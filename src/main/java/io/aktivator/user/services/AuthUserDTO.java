package io.aktivator.user.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class AuthUserDTO {
    private String externalId;
    private String name;
    private String surname;
    private String email;
    private String nickname;
    private String photoUrl;
    private Map<String, Object> metadata;
    private String longAddress;
}
