package org.igorski.springkeycloak.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.igorski.springkeycloak.configuration.json.UserIdSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    
    @JsonSerialize(using = UserIdSerializer.class)
    private String owner;
    @Column(nullable = false)
    private String text;
    @ManyToOne
    private Initiative initiative;
    @Column(nullable = false)
    private Date date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
            owner.equals(comment.owner) &&
            text.equals(comment.text) &&
            initiative.equals(comment.initiative) &&
            date.equals(comment.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owner, text, initiative, date);
    }
}
