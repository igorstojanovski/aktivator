package io.aktivator.campaign.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.aktivator.configuration.json.UserIdSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

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
    private Long campaignId;
    @Column(nullable = false)
    private Date date;
    private boolean visible = true;
}
