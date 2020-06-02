package io.aktivator.campaign.like;

import io.aktivator.campaign.donation.DonationCampaign;
import io.aktivator.campaign.donation.DonationCampaignRepository;
import io.aktivator.exceptions.ResourceAlreadyExists;
import io.aktivator.user.model.User;
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
        User currentUser = userService.getCurrentUser();
        if(donationCampaignRepository.findByIdAndLikes_Owner_Id(campaignId, currentUser.getId()).isPresent()) {
            throw new ResourceAlreadyExists("Like already exists for this user and this campaign.");
        }

        Like like = new Like();
        like.setOwner(currentUser);
        Like savedLike = likeRepository.save(like);
        DonationCampaign donationCampaign = donationCampaignRepository.findById(campaignId).get();
        donationCampaign.getLikes().add(savedLike);
        donationCampaignRepository.save(donationCampaign);
        return likeRepository.save(like);
    }
}
