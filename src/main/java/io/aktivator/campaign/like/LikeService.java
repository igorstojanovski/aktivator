package io.aktivator.campaign.like;

import io.aktivator.campaign.donation.DonationCampaign;
import io.aktivator.campaign.donation.DonationCampaignRepository;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private DonationCampaignRepository donationCampaignRepository;
    @Autowired
    private UserService userService;

    public Like createLike(Long campaignId) {
        Like like = new Like();
        like.setOwner(userService.getCurrentUser());
        Like savedLike = likeRepository.save(like);
        DonationCampaign donationCampaign = donationCampaignRepository.findById(campaignId).get();
        donationCampaign.getLikes().add(savedLike);
        donationCampaignRepository.save(donationCampaign);
        return likeRepository.save(like);
    }
}
