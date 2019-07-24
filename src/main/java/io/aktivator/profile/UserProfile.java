package io.aktivator.profile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String name;
    private String surname;
    private String email;
    private String username;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return id.equals(that.id) &&
            userId.equals(that.userId) &&
            name.equals(that.name) &&
            surname.equals(that.surname) &&
            email.equals(that.email) &&
            username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, name, surname, email, username);
    }
}
