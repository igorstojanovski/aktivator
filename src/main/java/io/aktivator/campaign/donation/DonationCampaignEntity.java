package io.aktivator.campaign.donation;

import io.aktivator.campaign.Campaign;
import io.aktivator.campaign.donation.payment.ExternalPaymentMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
class DonationCampaignEntity extends Campaign {
    @NotNull
    private Long target;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ExternalPaymentMethod> externalPaymentMethods;

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
