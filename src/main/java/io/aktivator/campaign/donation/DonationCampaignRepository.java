package io.aktivator.campaign.donation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface DonationCampaignRepository extends PagingAndSortingRepository<DonationCampaignEntity, Long> {
}
