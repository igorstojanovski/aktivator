package io.aktivator.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String name;
    private String lastName;
    private String surname;
    private String story;
}
