package io.aktivator.campaign.donation;

import io.aktivator.campaign.CampaignStatus;
import io.aktivator.campaign.like.Like;
import io.aktivator.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {
    @Autowired
    private DonationRepository donationRepository;

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
}
