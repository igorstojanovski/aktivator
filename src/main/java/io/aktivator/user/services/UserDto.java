package io.aktivator.user.services;

import com.fasterxml.jackson.annotation.JsonView;
import io.aktivator.user.UserViews;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class UserDto {
    @JsonView(UserViews.Partial.class)
    private final Long id;
    private final String name;
    private final String surname;
    private final String email;
    @JsonView(UserViews.Partial.class)
    private final String username;
    @JsonView(UserViews.Partial.class)
    private final String photoUrl;
    private final Map<String, Object> metadata;
    private final String longAddress;
}
