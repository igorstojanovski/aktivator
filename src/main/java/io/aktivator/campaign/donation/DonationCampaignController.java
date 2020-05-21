package io.aktivator.campaign.donation;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
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
public class DonationCampaignController {

    private UserService userService;
    private DonationCampaignService donationCampaignService;

    @Autowired
    DonationCampaignController(UserService userService, DonationCampaignService donationCampaignService) {
        this.userService = userService;
        this.donationCampaignService = donationCampaignService;
    }

    @PostMapping
    public ResponseEntity<DonationCampaign> createDonationCampaign(@RequestBody DonationCampaignCreateRequest createRequest) {
        User currentUser = userService.getCurrentUser();
        DonationCampaign saved = donationCampaignService.save(createRequest, currentUser.getId());

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<DonationCampaign> getCampaign(@PathVariable Long campaignId) {
        try {
            return new ResponseEntity<>(donationCampaignService.getCampaign(campaignId), HttpStatus.OK);
        } catch (DataException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public Page<DonationCampaign> getAllCampaigns(
        @SortDefault.SortDefaults({
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
        }) Pageable pageable) {
        return donationCampaignService.getAllCampaigns(pageable);
    }
}
