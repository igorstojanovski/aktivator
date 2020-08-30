package io.aktivator.campaign.donation;

import io.aktivator.campaign.CampaignStatus;
import io.aktivator.campaign.like.Like;
import io.aktivator.events.PaymentsService;
import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {
    @Autowired
    private UserService userService;
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private PaymentsService paymentsService;

    public DonationDto getCampaignDto(Long campaignId) throws DataException {
        Donation donation = getDonation(campaignId);
        return donationCampaignToDto(donation);
    }

    public Donation getDonation(Long campaignId) {
        return donationRepository.findById(campaignId)
                    .orElseThrow(() -> new DataException("No such campaign found."));
    }

    Donation save(DonationDto donationCampaignEntity, Long ownerId) {
        return donationRepository.save(creationRequestToEntity(donationCampaignEntity, ownerId));
    }

    private DonationDto donationCampaignToDto(Donation donation) {
        DonationDto campaignDto = new DonationDto();
        campaignDto.setId(donation.getId());
        campaignDto.setCreated(donation.getCreated());
        campaignDto.setDescription(donation.getDescription());
        campaignDto.setEndDate(donation.getEndDate());
        campaignDto.setFeatured(donation.isFeatured());
        campaignDto.setOwnerId(donation.getOwnerId());
        campaignDto.setTitle(donation.getTitle());
        campaignDto.setLiked(isCampaignLiked(donation.getLikes(), donation.getOwnerId()));
        if(donation.getLikes() != null) {
            campaignDto.setLikesCount(donation.getLikes().size());
        }
        campaignDto.setCampaignStatus(donation.getCampaignStatus());
        campaignDto.setBalance(paymentsService.getTotalDonations(donation.getId()));
        return campaignDto;
    }

    private boolean isCampaignLiked(List<Like> likes, Long ownerId) {
        if(likes != null) {
            for(Like like : likes) {
                if(like.getOwner().getId().equals(ownerId)) {
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

    Page<Donation> getAllCampaigns(Pageable pageable) {
        return donationRepository.findAll(pageable);
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

    public Page<Donation> getDonations(Pageable pageable, String internalUserId) {
        return donationRepository.findByOwnerId(Long.valueOf(internalUserId), pageable);
    }
}
