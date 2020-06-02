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
    public ResponseEntity<DonationCampaign> createDonationCampaign(@RequestBody DonationCampaignDto createRequest) {
        return createRequest.getId() == null
                ? saveCampaign(createRequest)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<DonationCampaign> saveCampaign(@RequestBody DonationCampaignDto createRequest) {
        ResponseEntity<DonationCampaign> donationCampaignResponseEntity;
        User currentUser = userService.getCurrentUser();
        DonationCampaign saved = donationCampaignService.save(createRequest, currentUser.getId());
        donationCampaignResponseEntity = new ResponseEntity<>(saved, HttpStatus.OK);
        return donationCampaignResponseEntity;
    }

    @PutMapping
    public ResponseEntity<DonationCampaign> updateDonationCampaign(@RequestBody DonationCampaignDto update) {
        return update.getId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : saveCampaign(update);
    }

    @GetMapping("/{campaignId}")
    public ResponseEntity<DonationCampaignDto> getCampaign(@PathVariable Long campaignId) {
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
