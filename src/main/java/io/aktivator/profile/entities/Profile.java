package io.aktivator.profile.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Profile {
    private String name;
    private String surname;
    private String email;
    private String username;
    private ExtendedProfile extendedProfile;
}
