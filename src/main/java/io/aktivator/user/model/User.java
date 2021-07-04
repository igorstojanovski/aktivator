package io.aktivator.user.model;

import com.fasterxml.jackson.annotation.JsonView;
import io.aktivator.user.UserViews;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "external_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(UserViews.Partial.class)
    private Long id;

    @Column(unique = true)
    @JsonView(UserViews.Full.class)
    private String externalId;
    @OneToOne(cascade=CascadeType.ALL)
    @JsonView(UserViews.Partial.class)
    private UserInformation userInformation;
}
