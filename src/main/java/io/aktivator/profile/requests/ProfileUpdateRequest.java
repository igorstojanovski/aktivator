package io.aktivator.profile.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String story;
}
