package io.aktivator.campaign.donation;

import io.aktivator.exceptions.DataException;
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

    DonationCampaign getCampaign(Long campaignId) throws DataException {
        return repository.findById(campaignId)
                .orElseThrow(() -> new DataException("No such campaign found."));
    }

    DonationCampaign save(DonationCampaignCreateRequest donationCampaignEntity, String ownerId) {
        return repository.save(creationRequestToEntity(donationCampaignEntity, ownerId));
    }

    private DonationCampaign creationRequestToEntity(DonationCampaignCreateRequest request, String ownerId) {
        DonationCampaign entity = new DonationCampaign();
        entity.setTarget(request.getTarget());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCreated(request.getCreated());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setOwner(ownerId);
        return entity;
    }

    Page<DonationCampaign> getAllCampaigns(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
