package io.aktivator.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private List<String> authorities;
}
