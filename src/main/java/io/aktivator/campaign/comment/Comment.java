package io.aktivator.campaign.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.aktivator.configuration.UserIdSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
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
    @JsonSerialize(converter = UserIdSerializer.class)
    private Long userId;
    @Column(nullable = false)
    private String text;
    private Long campaignId;
    @Column(nullable = false)
    @CreatedDate
    private Date date;
    private boolean visible = true;
}
