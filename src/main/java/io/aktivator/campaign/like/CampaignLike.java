package io.aktivator.campaign.like;

import io.aktivator.campaign.Campaign;
import io.aktivator.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class CampaignLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User owner;
    @OneToOne
    private Campaign campaign;
}
