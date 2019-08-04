package io.aktivator.campaign;

import io.aktivator.model.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    public Campaign getCampaign(Long campaignId) throws DataException {
        return campaignRepository.findById(campaignId).orElseThrow(() -> new DataException("No such campaign"));
    }
}
