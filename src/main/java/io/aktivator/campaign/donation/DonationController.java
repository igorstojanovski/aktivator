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
public class DonationController {

    private UserService userService;
    private DonationService donationService;

    @Autowired
    DonationController(UserService userService, DonationService donationService) {
        this.userService = userService;
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<Donation> createDonationCampaign(@RequestBody DonationDto createRequest) {
        return createRequest.getId() == null
                ? saveCampaign(createRequest)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Donation> saveCampaign(@RequestBody DonationDto createRequest) {
        ResponseEntity<Donation> donationCampaignResponseEntity;
        User currentUser = userService.getCurrentUser();
        Donation saved = donationService.save(createRequest, currentUser.getId());
        donationCampaignResponseEntity = new ResponseEntity<>(saved, HttpStatus.OK);
        return donationCampaignResponseEntity;
    }

    @PutMapping
    public ResponseEntity<Donation> updateDonationCampaign(@RequestBody DonationDto update) {
        return update.getId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : saveCampaign(update);
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
    public Page<Donation> getAllCampaigns(
        @SortDefault.SortDefaults({
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
        }) Pageable pageable) {
        return donationService.getAllCampaigns(pageable);
    }
}
