package io.aktivator;

import io.aktivator.campaign.donation.Donation;
import io.aktivator.campaign.donation.DonationDto;
import io.aktivator.campaign.donation.DonationService;
import io.aktivator.events.PaymentSubmittedEventDTO;
import io.aktivator.events.PaymentSubmittedService;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;

@SpringBootApplication
public class AktivatorApiApplication {

	@Autowired
	private UserService userService;
	@Autowired
	private DonationService donationService;
	@Autowired
	private PaymentSubmittedService paymentSubmittedService;

	public static void main(String[] args) {
		SpringApplication.run(AktivatorApiApplication.class, args);
	}

	@PostConstruct
	public void initTestData() {
		User registeredUser = userService.registerUser("auth0|5eac8eda02b1770be4749949");
		DonationDto createRequest = new DonationDto();
		createRequest.setOwnerId(registeredUser.getId());
		createRequest.setDescription("Test Donation Campaign");
		createRequest.setTitle("Save the World!");
		createRequest.setTarget(1000L);

		createRequest.setCreated(new Date());
		createRequest.setStartDate(getDate(DateTime.now().plusDays(1)));
		createRequest.setEndDate(getDate(DateTime.now().plusDays(14)));
		ResponseEntity<Donation> campaign = donationService.saveCampaign(createRequest, registeredUser);

		PaymentSubmittedEventDTO paymentEvent = new PaymentSubmittedEventDTO();
		paymentEvent.setCampaignId(campaign.getBody().getId());
		paymentEvent.setAmount(BigDecimal.valueOf(100L));
		paymentSubmittedService.addPaymentSubmittedEvent(paymentEvent, registeredUser);

		paymentEvent = new PaymentSubmittedEventDTO();
		paymentEvent.setCampaignId(campaign.getBody().getId());
		paymentEvent.setAmount(BigDecimal.valueOf(220L));
		paymentSubmittedService.addPaymentSubmittedEvent(paymentEvent, registeredUser);
	}

	private Date getDate(DateTime dateRedeemed) {
		DateTime dateTimeBerlin = dateRedeemed.withZone(DateTimeZone.forID("Europe/Berlin"));
		return dateTimeBerlin.toLocalDateTime().toDate();
	}

}
