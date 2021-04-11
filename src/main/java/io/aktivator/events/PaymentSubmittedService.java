package io.aktivator.events;

import io.aktivator.campaign.donation.Donation;
import io.aktivator.campaign.donation.DonationService;
import io.aktivator.user.model.User;
import io.aktivator.user.services.AuthorizationServiceException;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        addPaymentSubmittedEvent(paymentSubmittedEventDTO, userService.getCurrentUser());
    }

    public void addPaymentSubmittedEvent(PaymentSubmittedEventDTO paymentSubmittedEventDTO, User currentUser) {
        PaymentSubmittedEvent paymentSubmittedEvent = convertToPaymentSubmittedEvent(paymentSubmittedEventDTO, currentUser);
        eventRepository.save(paymentSubmittedEvent);
    }

    private PaymentSubmittedEvent convertToPaymentSubmittedEvent(PaymentSubmittedEventDTO paymentSubmittedEventDTO, User user) {
        Donation donation = donationService.getDonation(paymentSubmittedEventDTO.getCampaignId());

        PaymentSubmittedEvent paymentSubmittedEvent = new PaymentSubmittedEvent();
        paymentSubmittedEvent.setAmount(paymentSubmittedEventDTO.getAmount());
        paymentSubmittedEvent.setDonation(donation);
        paymentSubmittedEvent.setUser(user);
        paymentSubmittedEvent.setDate(new Date());

        return paymentSubmittedEvent;
    }

    BigDecimal getSubmittedPaymentsBalance(Long campaignId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PaymentSubmittedEvent> events = paymentSubmittedEventRepository.findByDonationId(campaignId);

        for(PaymentSubmittedEvent event : events) {
            totalAmount = totalAmount.add(event.getAmount());
        }

        return totalAmount;
    }

    public List<PaymentDto> getAllSubmittedPayments(Long campaignId) {
        List<PaymentSubmittedEvent> events = paymentSubmittedEventRepository.findByDonationId(campaignId);
        List<PaymentDto> submittedPayments = new ArrayList<>();

        for(PaymentSubmittedEvent submittedEvent : events) {
            String externalId = submittedEvent.getUser().getExternalId();
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setAmount(submittedEvent.getAmount());
            paymentDto.setDate(submittedEvent.getDate());
            try {
                paymentDto.setDonor(userService.getUserInformation(externalId));

            } catch (AuthorizationServiceException e) {
                e.printStackTrace();
            }
        }
        return submittedPayments;
    }
}
