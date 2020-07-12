package io.aktivator.events;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class PaymentSubmittedEventDTO {
    private Long campaignId;
    private BigDecimal amount;
}
