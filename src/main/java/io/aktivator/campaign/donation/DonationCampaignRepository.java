package io.aktivator.campaign.donation;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DonationCampaignRepository extends PagingAndSortingRepository<DonationCampaign, Long> {
    Optional<DonationCampaign> findByIdAndLikes_Owner_Id(Long id, Long ownerId);
}
