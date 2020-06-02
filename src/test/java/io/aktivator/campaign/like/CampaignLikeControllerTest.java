package io.aktivator.campaign.like;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignLikeControllerTest {

    public static final long CAMPAIGN_ID = 1L;
    public static final long LIKE_ID = 2L;
    @Mock
    private LikeService likeService;
    @InjectMocks
    private LikeController likeController;

    @Test
    void shouldCallServiceToCreateLike() {
        CampaignLike campaignLike = new CampaignLike();
        campaignLike.setId(LIKE_ID);

        when(likeService.createLike(CAMPAIGN_ID)).thenReturn(campaignLike);
        ResponseEntity<CampaignLike> response = likeController.createLike(CAMPAIGN_ID);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(LIKE_ID);
    }

}