package io.aktivator.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/campaign/{campaignId}/payment")
public class PaymentsController {

    @Autowired
    private PaymentsService paymentsService;

    @GetMapping
    public ResponseEntity<List<PaymentDto> > getAllPayments(@PathVariable Long campaignId) {
        List<PaymentDto> allPayments = paymentsService.getAllPayments(campaignId);
        return new ResponseEntity<>(allPayments, HttpStatus.OK);
    }

}
