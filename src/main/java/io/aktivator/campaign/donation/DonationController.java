package io.aktivator.campaign.donation;

import io.aktivator.exceptions.DataException;
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

    private DonationService donationService;

    @Autowired
    DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    public ResponseEntity<Donation> createDonationCampaign(@RequestBody DonationDto createRequest) {
        return createRequest.getId() == null
                ? donationService.saveCampaign(createRequest)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    public ResponseEntity<Donation> updateDonationCampaign(@RequestBody DonationDto update) {
        return update.getId() == null
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : donationService.saveCampaign(update);
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
    public Page<Donation> getDonations(
        @SortDefault.SortDefaults({
            @SortDefault(sort = "id", direction = Sort.Direction.DESC)
        }) Pageable pageable, @RequestParam(required = false) String userId) {
        if(userId != null) {
            return donationService.getDonations(pageable, userId);
        } else {
            return donationService.getAllCampaigns(pageable);
        }
    }
}
