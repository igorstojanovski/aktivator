package io.aktivator.campaign;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.aktivator.campaign.like.Like;
import io.aktivator.configuration.UserIdSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Inheritance(
    strategy = InheritanceType.JOINED
)
@Entity
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Long ownerId;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull
    private Date endDate;
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created = new Date();
    private boolean featured;
    @OneToMany
    private List<Like> likes;
    @Enumerated(EnumType.STRING)
    private CampaignStatus campaignStatus;
}
