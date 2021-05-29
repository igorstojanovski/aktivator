package io.aktivator.campaign.donation;

import io.aktivator.campaign.CampaignStatus;
import io.aktivator.campaign.like.Like;
import io.aktivator.events.PaymentsService;
import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationService {
  @Autowired private UserService userService;
  @Autowired private DonationRepository donationRepository;
  @Autowired private PaymentsService paymentsService;

  public DonationDto getCampaignDto(Long campaignId) throws DataException {
    Donation donation = getDonation(campaignId);
    return donationCampaignToDto(donation);
  }

  public Donation getDonation(Long campaignId) {
    return donationRepository
        .findById(campaignId)
        .orElseThrow(() -> new DataException("No such campaign found."));
  }

  Donation save(DonationDto donationCampaignEntity, Long ownerId) {
    return donationRepository.save(creationRequestToEntity(donationCampaignEntity, ownerId));
  }

  private DonationDto donationCampaignToDto(Donation donation) {
    return new DonationDto(
        donation.getId(),
        donation.getTitle(),
        donation.getDescription(),
        donation.getOwnerId(),
        donation.getStartDate(),
        donation.getEndDate(),
        donation.getCreated(),
        donation.getTarget(),
        donation.isFeatured(),
        isCampaignLiked(donation.getLikes(), donation.getOwnerId()),
        donation.getLikes().size(),
        donation.getCampaignStatus(),
        paymentsService.getTotalDonations(donation.getId()));
  }

  private boolean isCampaignLiked(List<Like> likes, Long ownerId) {
    if (likes != null) {
      for (Like like : likes) {
        if (like.getOwner().getId().equals(ownerId)) {
          return true;
        }
      }
    }
    return false;
  }

  private Donation creationRequestToEntity(DonationDto request, Long ownerId) {
    Donation entity = new Donation();
    entity.setTarget(request.getTarget());
    entity.setTitle(request.getTitle());
    entity.setDescription(request.getDescription());
    entity.setCreated(request.getCreated());
    entity.setStartDate(request.getStartDate());
    entity.setEndDate(request.getEndDate());
    entity.setOwnerId(ownerId);
    entity.setCampaignStatus(CampaignStatus.NEW);
    return entity;
  }

  PageImpl<DonationDto> getAllDonations(Pageable pageable) {
    Page<Donation> allDonations = donationRepository.findAll(pageable);
    return new PageImpl<>(
        allDonations.get().map(this::donationCampaignToDto).collect(Collectors.toList()),
        allDonations.getPageable(),
        allDonations.getTotalPages());
  }

  public ResponseEntity<Donation> saveCampaign(DonationDto createRequest) {
    return saveCampaign(createRequest, userService.getCurrentUser());
  }

  public ResponseEntity<Donation> saveCampaign(DonationDto createRequest, User currentUser) {
    ResponseEntity<Donation> donationCampaignResponseEntity;
    Donation saved = save(createRequest, currentUser.getId());
    donationCampaignResponseEntity = new ResponseEntity<>(saved, HttpStatus.OK);
    return donationCampaignResponseEntity;
  }

  public Page<DonationDto> getDonations(Pageable pageable, String internalUserId) {
    Page<Donation> allDonations =
        donationRepository.findByOwnerId(Long.valueOf(internalUserId), pageable);
    return new PageImpl<>(
        allDonations.get().map(this::donationCampaignToDto).collect(Collectors.toList()),
        allDonations.getPageable(),
        allDonations.getTotalPages());
  }
}
