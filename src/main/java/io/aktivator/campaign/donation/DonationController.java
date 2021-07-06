package io.aktivator.campaign.donation;

import io.aktivator.exceptions.DataException;
import io.aktivator.exceptions.DataValidationException;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/campaign/donation")
public class DonationController {

  private final UserService userService;
  private final DonationService donationService;

  @Autowired
  DonationController(DonationService donationService, UserService userService) {
    this.donationService = donationService;
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<Donation> createDonationCampaign(@RequestBody DonationDto createRequest) {
    return createRequest.getId() == null
        ? donationService.createCampaign(createRequest)
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PatchMapping("/{campaignId}")
  public ResponseEntity<Donation> updateDonationCampaign(
      @RequestBody DonationUpdateDto update, @PathVariable Long campaignId) {
    if (!update.getId().equals(campaignId)) {
      throw new DataValidationException("Wrong campaign ID.");
    }
    if (!userService
        .getCurrentUser()
        .getId()
        .equals(donationService.getDonation(campaignId).getOwnerId())) {
      return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    return update.getId() == null
        ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
        : new ResponseEntity<>(donationService.updateCampaign(update), HttpStatus.OK);
  }

  @GetMapping("/{campaignId}")
  public ResponseEntity<DonationDto> getCampaign(@PathVariable Long campaignId) {
    try {
      return new ResponseEntity<>(donationService.getCampaignDto(campaignId), HttpStatus.OK);
    } catch (DataException e) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping
  public Page<DonationDto> getDonations(
      @SortDefault.SortDefaults({@SortDefault(sort = "id", direction = Sort.Direction.DESC)})
          Pageable pageable,
      @RequestParam(required = false) String userId) {
    if (userId != null) {
      return donationService.getDonations(pageable, userId);
    } else {
      return donationService.getAllDonations(pageable);
    }
  }
}
