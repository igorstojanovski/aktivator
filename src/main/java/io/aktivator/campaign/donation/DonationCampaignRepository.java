package io.aktivator.campaign.donation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationCampaignRepository extends PagingAndSortingRepository<DonationCampaign, Long> {
}
