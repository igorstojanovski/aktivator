package io.aktivator.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaign/{campaignId}/event/payment")
public class PaymentEventController {

    @Autowired
    private PaymentSubmittedService paymentSubmittedService;

    @PostMapping("/submitted")
    public ResponseEntity<String> paymentSubmitted(@PathVariable Long campaignId,
                                                   @RequestBody PaymentSubmittedEventDTO paymentSubmittedEventDTO) {
        paymentSubmittedEventDTO.setCampaignId(campaignId);
        paymentSubmittedService.addPaymentSubmittedEvent(paymentSubmittedEventDTO);
        return new ResponseEntity<>("Payment submitted event saved.", HttpStatus.OK);
    }
}
