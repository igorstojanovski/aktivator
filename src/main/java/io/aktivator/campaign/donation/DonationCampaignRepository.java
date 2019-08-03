package io.aktivator.campaign.donation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DonationCampaignRepository extends CrudRepository<DonationCampaignEntity, Long> {
}
