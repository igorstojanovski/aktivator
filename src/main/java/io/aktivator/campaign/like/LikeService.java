package io.aktivator.campaign.like;

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

    public CampaignLike createLike(Long campaignId) {
        CampaignLike campaignLike = new CampaignLike();
        campaignLike.setOwner(userService.getCurrentUser());
        campaignLike.setCampaign(donationCampaignRepository.findById(campaignId).get());

        return likeRepository.save(campaignLike);
    }

    public boolean isCampaignLiked(Long campaignId) {
        CampaignLike campaignLike = likeRepository.findByOwnerIdAndCampaignId(userService.getCurrentUser().getId(), campaignId);

        return campaignLike != null;
    }
}
