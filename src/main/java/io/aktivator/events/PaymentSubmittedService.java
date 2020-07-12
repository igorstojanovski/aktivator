package io.aktivator.events;

import io.aktivator.campaign.donation.Donation;
import io.aktivator.campaign.donation.DonationService;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PaymentSubmittedService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PaymentSubmittedEventRepository paymentSubmittedEventRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DonationService donationService;

    public void addPaymentSubmittedEvent(PaymentSubmittedEventDTO paymentSubmittedEventDTO) {
        PaymentSubmittedEvent paymentSubmittedEvent = convertToPaymentSubmittedEvent(paymentSubmittedEventDTO);
        eventRepository.save(paymentSubmittedEvent);
    }

    private PaymentSubmittedEvent convertToPaymentSubmittedEvent(PaymentSubmittedEventDTO paymentSubmittedEventDTO) {
        User user = userService.getCurrentUser();
        Donation donation = donationService.getDonation(paymentSubmittedEventDTO.getCampaignId());

        PaymentSubmittedEvent paymentSubmittedEvent = new PaymentSubmittedEvent();
        paymentSubmittedEvent.setAmount(paymentSubmittedEventDTO.getAmount());
        paymentSubmittedEvent.setDonation(donation);
        paymentSubmittedEvent.setUser(user);
        paymentSubmittedEvent.setDate(new Date());

        return paymentSubmittedEvent;
    }

    public BigDecimal getSubmittedPaymentsBalance(Long campaignId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PaymentSubmittedEvent> events = paymentSubmittedEventRepository.findByDonationId(campaignId);

        for(PaymentSubmittedEvent event : events) {
            totalAmount = totalAmount.add(event.getAmount());
        }

        return totalAmount;
    }
}
