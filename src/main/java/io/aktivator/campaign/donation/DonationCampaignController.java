package io.aktivator.campaign.donation;

import io.aktivator.model.DataException;
import io.aktivator.profile.Roles;
import io.aktivator.user.model.UserDTO;
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
    public ResponseEntity<DonationCampaignEntity> createDonationCampaign(@RequestBody DonationCampaignCreateRequest createRequest) {
        UserDTO currentUser = userService.getCurrentUser();
        if (!currentUser.getAuthorities().contains(Roles.ACTIVIST.name())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        DonationCampaignEntity saved = donationCampaignService.save(createRequest, currentUser.getId());

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

    @GetMapping
    public Page<DonationCampaignEntity> getAllCampaigns(
        @SortDefault.SortDefaults({
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
        }) Pageable pageable) {
        return donationCampaignService.getAllCampaigns(pageable);
    }
}
