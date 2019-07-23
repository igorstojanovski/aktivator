package io.aktivator.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileUpdateRequest {
    private String name;
    private String surname;
    private String email;
    private String username;
}
