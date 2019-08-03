package io.aktivator.campaign.donation;

import io.aktivator.model.DataException;
import org.springframework.beans.factory.annotation.Autowired;
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

    DonationCampaignEntity save(DonationCampaignCreateRequest donationCampaignEntity) {
        return repository.save(creationRequestToEntity(donationCampaignEntity));
    }

    private DonationCampaignEntity creationRequestToEntity(DonationCampaignCreateRequest request) {
        DonationCampaignEntity entity = new DonationCampaignEntity();
        entity.setTarget(request.getTarget());
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setCreated(request.getCreated());
        entity.setStartDate(request.getStartDate());
        entity.setEndDate(request.getEndDate());
        entity.setOwnerId(request.getOwnerId());

        return entity;
    }
}
