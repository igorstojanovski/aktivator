package io.aktivator.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
class Profile {
    private String name;
    private String surname;
    private String email;
    private String username;
    private ExtendedProfile extendedProfile;
}
