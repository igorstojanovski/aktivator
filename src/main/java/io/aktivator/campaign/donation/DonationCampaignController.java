package io.aktivator.campaign.donation;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import io.aktivator.profile.Roles;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/campaign/donation")
public class DonationCampaignController {

    private UserService userService;
    private DonationCampaignService donationCampaignService;

    @Autowired
    DonationCampaignController(UserService userService, DonationCampaignService donationCampaignService) {
        this.userService = userService;
        this.donationCampaignService = donationCampaignService;
    }

    @PostMapping
    public ResponseEntity<DonationCampaignEntity> createDonationCampaign(@RequestBody DonationCampaignCreateRequest createRequest) {
        UserDTO currentUser = userService.getCurrentUser();
        if (!currentUser.getAuthorities().contains(Roles.ACTIVIST.name())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        createRequest.setOwnerId(currentUser.getId());
        DonationCampaignEntity saved = donationCampaignService.save(createRequest);

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<DonationCampaignEntity> getCampaign(@PathVariable Long campaignId) {
        try {
            return new ResponseEntity<>(donationCampaignService.getCampaign(campaignId), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
