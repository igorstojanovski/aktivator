package io.aktivator.campaign.donation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationCampaignRepository extends CrudRepository<DonationCampaign, Long> {
}
