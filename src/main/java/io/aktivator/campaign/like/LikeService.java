package io.aktivator.campaign.like;

import io.aktivator.campaign.donation.Donation;
import io.aktivator.campaign.donation.DonationRepository;
import io.aktivator.exceptions.DataException;
import io.aktivator.exceptions.ResourceAlreadyExists;
import io.aktivator.notifications.NotificationService;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

  @Autowired private LikeRepository likeRepository;
  @Autowired private DonationRepository donationRepository;
  @Autowired private UserService userService;
  @Autowired private NotificationService notificationService;

  public Like createLike(Long campaignId) {
    User currentUser = userService.getCurrentUser();
    if (donationRepository.findByIdAndLikes_Owner_Id(campaignId, currentUser.getId()).isPresent()) {
      throw new ResourceAlreadyExists("Like already exists for this user and this campaign.");
    }

    Like like = new Like();
    like.setOwner(currentUser);
    Like savedLike = likeRepository.save(like);
    Donation donation = donationRepository.findById(campaignId).get();
    donation.getLikes().add(savedLike);
    donationRepository.save(donation);
    notificationService.createLikeNotification(currentUser, donation.getOwnerId());
    return savedLike;
  }

  public List<Like> getLikes(Long campaignId) {
    Donation donation =
        donationRepository
            .findById(campaignId)
            .orElseThrow(() -> new DataException("Campaign does not exist."));

    return donation.getLikes();
  }

  public void removeLike(Long campaignId) {
    User currentUser = userService.getCurrentUser();
    Donation donation =
        donationRepository
            .findById(campaignId)
            .orElseThrow(() -> new DataException("No such campaign ID found."));
    if(!donation.getLikes().removeIf(l -> l.getOwner().getId().equals(currentUser.getId()))) {
      throw new DataException("No like was found. No like was removed.");
    };
    donationRepository.save(donation);
  }
}
