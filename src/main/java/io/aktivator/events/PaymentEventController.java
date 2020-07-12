package io.aktivator.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/api/campaign/{campaignId}/payment")
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

    @GetMapping("/submitted")
    public ResponseEntity<SubmittedPaymentsBalanceDTO> getSubmittedAmountsBalance(@PathVariable Long campaignId) {
        BigDecimal amount = paymentSubmittedService.getSubmittedPaymentsBalance(campaignId);
        SubmittedPaymentsBalanceDTO submittedPaymentsBalance = new SubmittedPaymentsBalanceDTO();
        submittedPaymentsBalance.setAmount(amount);
        submittedPaymentsBalance.setDate(new Date());
        return new ResponseEntity<>(submittedPaymentsBalance, HttpStatus.OK);
    }
}
