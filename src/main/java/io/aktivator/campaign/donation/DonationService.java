package io.aktivator.campaign.donation;

import io.aktivator.campaign.CampaignStatus;
import io.aktivator.campaign.like.Like;
import io.aktivator.events.PaymentsService;
import io.aktivator.exceptions.DataException;
import io.aktivator.exceptions.DataValidationException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
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

  public Donation updateCampaign(DonationUpdateDto updateRequest) {
    Donation donation =
        donationRepository
            .findById(updateRequest.getId())
            .orElseThrow(
                () -> new DataException("No such campaign was found: " + updateRequest.getId()));

    if (donation.getCampaignStatus() == CampaignStatus.EXPIRED) {
      throw new DataValidationException("You cannot edit an expired campaign.");
    }

    if (isValidStartDateEditRequest(updateRequest, donation)) {
      donation.setStartDate(updateRequest.getStartDate());
    }

    if (isValidEndDateUpdateRequest(updateRequest, donation)) {
      donation.setEndDate(updateRequest.getEndDate());
    }

    if (isValidDescriptionEditRequest(updateRequest, donation)) {
      donation.setDescription(updateRequest.getDescription());
    }
    return donationRepository.save(donation);
  }

  private boolean isValidEndDateUpdateRequest(DonationUpdateDto updateRequest, Donation donation) {
    boolean isValidEndDateEditRequest = false;
    if (isEndDateEditRequest(updateRequest, donation)) {
      if (donation.getCampaignStatus() == CampaignStatus.EXPIRED) {
        throw new DataValidationException(
            "You can't change the end date to an already expired campaign.");
      } else if (updateRequest.getEndDate().before(new Date())
          || updateRequest.getEndDate().before(updateRequest.getStartDate())) {
        throw new DataValidationException(
            "Campaign end date needs to be in the future and after the start date.");
      }
      isValidEndDateEditRequest = true;
    }

    return isValidEndDateEditRequest;
  }

  private boolean isEndDateEditRequest(DonationUpdateDto updateRequest, Donation donation) {
    return updateRequest.getEndDate() != null
        && !donation.getEndDate().equals(updateRequest.getEndDate());
  }

  private boolean isValidDescriptionEditRequest(
      DonationUpdateDto updateRequest, Donation donation) {
    boolean isValidDescriptionEditRequest = false;
    if (isDescriptionEditRequest(updateRequest, donation)) {
      if (donation.getCampaignStatus() != CampaignStatus.NEW) {
        throw new DataValidationException(
            "The description can be changed only on an campaign that is not started.");
      }
      if (updateRequest.getDescription().length() < 128) {
        throw new DataValidationException(
            "The campaign description needs to contain more than 128 characters.");
      }
      isValidDescriptionEditRequest = true;
    }

    return isValidDescriptionEditRequest;
  }

  private boolean isDescriptionEditRequest(DonationUpdateDto updateRequest, Donation donation) {
    return updateRequest.getDescription() != null
        && !donation.getDescription().equals(updateRequest.getDescription());
  }

  private boolean isValidStartDateEditRequest(DonationUpdateDto updateRequest, Donation donation) {
    boolean isValidStartDateEditRequest = false;
    if (isTryingToUpdateStartDate(updateRequest, donation)) {
      if (donation.getCampaignStatus() != CampaignStatus.NEW) {
        throw new DataValidationException(
            "You can only change the start date of a campaign that is not started.");
      } else if (new Date().after(updateRequest.getStartDate())) {
        throw new DataValidationException("You cannot set the start date in the past.");
      }
      isValidStartDateEditRequest = true;
    }

    return isValidStartDateEditRequest;
  }

  private boolean isTryingToUpdateStartDate(DonationUpdateDto updateRequest, Donation donation) {
    return updateRequest.getStartDate() != null
        && !donation.getStartDate().equals(updateRequest.getStartDate());
  }

  public ResponseEntity<Donation> createCampaign(DonationDto createRequest) {
    return createCampaign(createRequest, userService.getCurrentUser());
  }

  public ResponseEntity<Donation> createCampaign(DonationDto request, User currentUser) {
    Donation saved = save(request, currentUser.getId());
    return new ResponseEntity<>(saved, HttpStatus.OK);
  }

  public Page<DonationDto> getDonations(Pageable pageable, String internalUserId) {
    Page<Donation> allDonations =
        donationRepository.findByOwnerId(Long.valueOf(internalUserId), pageable);
    return new PageImpl<>(
        allDonations.get().map(this::donationCampaignToDto).collect(Collectors.toList()),
        allDonations.getPageable(),
        allDonations.getTotalPages());
  }

  public List<Donation> getDonations(CampaignStatus status) {
    return donationRepository.findByCampaignStatus(status);
  }

  public void saveAll(List<Donation> donations) {
    donationRepository.saveAll(donations);
  }
}
