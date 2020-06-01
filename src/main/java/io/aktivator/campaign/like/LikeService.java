package io.aktivator.campaign.like;

import io.aktivator.campaign.donation.DonationCampaignService;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private DonationCampaignService donationCampaignService;

    public Like createLike(Long campaignId) {
        Like like = new Like();
        like.setUser(userService.getCurrentUser());
        like.setCampaign(donationCampaignService.getCampaign(campaignId));

        return likeRepository.save(like);
    }
}
