package io.aktivator.campaign.donation;

import io.aktivator.campaign.Campaign;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
class DonationCampaignEntity extends Campaign {
    @NotNull
    private Long target;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DonationCampaignEntity)) return false;
        if (!super.equals(o)) return false;
        DonationCampaignEntity entity = (DonationCampaignEntity) o;
        return Objects.equals(target, entity.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), target);
    }
}
