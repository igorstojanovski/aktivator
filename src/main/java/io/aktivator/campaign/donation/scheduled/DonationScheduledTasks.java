package io.aktivator.campaign.donation.scheduled;

import io.aktivator.campaign.CampaignStatus;
import io.aktivator.campaign.donation.Donation;
import io.aktivator.campaign.donation.DonationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DonationScheduledTasks {
  private static final Logger LOGGER = LoggerFactory.getLogger(DonationScheduledTasks.class);
  @Autowired private DonationService donationService;

  @Scheduled(fixedDelay = 60000)
  public void autoStartScheduledDonationCampaigns() {
    List<Donation> newDonations = donationService.getDonations(CampaignStatus.NEW);
    List<Donation> updatedDonations =
        newDonations.stream()
            .filter(d -> d.getStartDate().before(new Date()))
            .peek(d -> d.setCampaignStatus(CampaignStatus.ACTIVE))
            .collect(Collectors.toList());
    for (Donation donation : updatedDonations) {
      LOGGER.debug("Will set to active: {}", donation.getId());
    }
    if (!updatedDonations.isEmpty()) {
      donationService.saveAll(updatedDonations);
      autoExpireDonationCampaigns();
    }
  }

  @Scheduled(fixedDelay = 60000)
  public void autoExpireDonationCampaigns() {
    List<Donation> newDonations = donationService.getDonations(CampaignStatus.ACTIVE);
    List<Donation> updatedDonations =
        newDonations.stream()
            .filter(d -> d.getEndDate().before(new Date()))
            .peek(d -> d.setCampaignStatus(CampaignStatus.EXPIRED))
            .collect(Collectors.toList());
    for (Donation donation : updatedDonations) {
      LOGGER.debug("Will set to expired: {}", donation.getId());
    }

    if (!updatedDonations.isEmpty()) {
      donationService.saveAll(updatedDonations);
    }
  }
}
