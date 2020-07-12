package io.aktivator.events;

import io.aktivator.campaign.donation.Donation;
import io.aktivator.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PaymentSubmittedEvent extends Event {
    @OneToOne
    private User user;
    private BigDecimal amount;
    @OneToOne
    private Donation donation;
}
