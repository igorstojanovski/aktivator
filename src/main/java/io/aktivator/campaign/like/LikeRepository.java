package io.aktivator.campaign.like;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends CrudRepository<CampaignLike, Long> {
    CampaignLike findByOwnerIdAndCampaignId(Long userId, Long campaignId);
}
