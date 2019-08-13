package io.aktivator.campaign.donation;

import io.aktivator.model.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
class DonationCampaignService {
    private DonationCampaignRepository repository;

    @Autowired
    DonationCampaignService(DonationCampaignRepository repository) {
        this.repository = repository;
    }

    DonationCampaignEntity getCampaign(Long campaignId) throws DataException {
        return repository.findById(campaignId)
                .orElseThrow(() -> new DataException("No such campaign found."));
    }

    DonationCampaignEntity save(DonationCampaignCreateRequest donationCampaignEntity, String ownerId) {
        return repository.save(creationRequestToEntity(donationCampaignEntity, ownerId));
    }

    private DonationCampaignEntity creationRequestToEntity(DonationCampaignCreateRequest request, String ownerId) {
        DonationCampaignEntity entity = new DonationCampaignEntity();
        entity.setTarget(request.getTarget());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCreated(request.getCreated());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setOwnerId(ownerId);
        return entity;
    }

    Page<DonationCampaignEntity> getAllCampaigns(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
