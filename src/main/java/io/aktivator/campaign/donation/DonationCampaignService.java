package io.aktivator.campaign.donation;

import io.aktivator.campaign.like.LikeService;
import io.aktivator.exceptions.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DonationCampaignService {
    @Autowired
    private LikeService likeService;
    @Autowired
    private DonationCampaignRepository repository;

    public DonationCampaignDto getCampaign(Long campaignId) throws DataException {
        DonationCampaign donationCampaign = repository.findById(campaignId)
                .orElseThrow(() -> new DataException("No such campaign found."));
        return donationCampaignToDto(donationCampaign);
    }

    DonationCampaign save(DonationCampaignDto donationCampaignEntity, Long ownerId) {
        return repository.save(creationRequestToEntity(donationCampaignEntity, ownerId));
    }

    private DonationCampaignDto donationCampaignToDto(DonationCampaign donationCampaign) {
        DonationCampaignDto campaignDto = new DonationCampaignDto();
        campaignDto.setId(donationCampaign.getId());
        campaignDto.setCreated(donationCampaign.getCreated());
        campaignDto.setDescription(donationCampaign.getDescription());
        campaignDto.setEndDate(donationCampaign.getEndDate());
        campaignDto.setFeatured(donationCampaign.isFeatured());
        campaignDto.setOwnerId(donationCampaign.getOwnerId());
        campaignDto.setTitle(donationCampaign.getTitle());
        campaignDto.setLiked(likeService.isCampaignLiked(donationCampaign.getId()));

        return campaignDto;
    }

    private DonationCampaign creationRequestToEntity(DonationCampaignDto request, Long ownerId) {
        DonationCampaign entity = new DonationCampaign();
        entity.setTarget(request.getTarget());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCreated(request.getCreated());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setOwnerId(ownerId);
        return entity;
    }

    Page<DonationCampaign> getAllCampaigns(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
