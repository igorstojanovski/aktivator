package io.aktivator.user.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthUserDTO {
    private String name;
    private String surname;
    private String email;
    private String nickname;
    private String photoUrl;
}
