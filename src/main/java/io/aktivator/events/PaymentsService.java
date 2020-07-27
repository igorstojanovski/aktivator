package io.aktivator.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentsService {

    @Autowired
    private PaymentSubmittedService paymentSubmittedService;

    public BigDecimal getTotalDonations(Long id) {
        return paymentSubmittedService.getSubmittedPaymentsBalance(id);
    }

    public List<PaymentDto> getAllPayments(Long campaignId) {
        return paymentSubmittedService.getAllSubmittedPayments(campaignId);
    }
}
