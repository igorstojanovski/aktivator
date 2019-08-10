package io.aktivator.profile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String story;
}
