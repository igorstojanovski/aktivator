package io.aktivator.model;

import io.aktivator.profile.ProfileType;

import java.util.List;

public class UserDTO {
    private String id;
    private String name;
    private String surname;
    private String email;
    private String username;
    private List<ProfileType> types;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public List<ProfileType> getTypes() {
        return types;
    }

    public void setTypes(List<ProfileType> types) {
        this.types = types;
    }
}
