package io.aktivator.campaign.donation;

import io.aktivator.model.UserDTO;
import io.aktivator.profile.Roles;
import io.aktivator.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/campaign/donation")
public class DonationCampaignController {

    @Autowired
    private UserService userService;
    @Autowired
    private DonationCampaignRepository repository;

    @PostMapping
    public ResponseEntity<DonationCampaign> createDonationCampaign(@RequestBody DonationCampaign donationCampaign) {
        UserDTO currentUser = userService.getCurrentUser();
        if (!currentUser.getAuthorities().contains(Roles.ACTIVIST.name())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        donationCampaign.setOwnerId(currentUser.getId());
        DonationCampaign saved = repository.save(donationCampaign);

        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

}
