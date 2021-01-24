package io.aktivator.campaign.donation;

import io.aktivator.campaign.like.Like;
import io.aktivator.events.PaymentsService;
import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    public static final long OWNER_ID = 1L;
    public static final long CAMPAIGN_ID = 123L;
    @Mock
    private DonationRepository repository;
    @Mock
    private PaymentsService paymentsService;
    @InjectMocks
    private DonationService service;
    private Donation entity;
    private DonationDto createRequest;
    @Captor
    private ArgumentCaptor<Donation> entityArgumentCaptor = ArgumentCaptor.forClass(Donation.class);

    @BeforeEach
    void setupTest() {
        Date created = new Date();
        Date startDate = new Date();
        Date endDate = convertLocalDateToDate(LocalDate.now().plusDays(30));

        entity = new Donation();
        entity.setTarget(2000L);
        entity.setCreated(created);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setOwnerId(OWNER_ID);
        entity.setDescription("Quo vadis?!");
        entity.setTitle("Latin lessons donation");

        createRequest = new DonationDto();
        createRequest.setTarget(2000L);
        createRequest.setCreated(created);
        createRequest.setStartDate(startDate);
        createRequest.setEndDate(endDate);
        createRequest.setDescription("Quo vadis?!");
        createRequest.setTitle("Latin lessons donation");
    }

    private Date convertLocalDateToDate(LocalDate myLocalDate) {
        return Date.from(myLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    @Test
    void shouldGoToRepoToSave() {
        service.save(createRequest, 1L);
        verify(repository).save(entityArgumentCaptor.capture());

        Donation value = entityArgumentCaptor.getValue();
        assertThat(value.getTarget()).isEqualTo(2000L);
    }

    @Test
    void shouldGoToRepoToGetById() throws DataException {
        when(repository.findById(CAMPAIGN_ID)).thenReturn(Optional.of(entity));
        DonationDto found = service.getCampaignDto(CAMPAIGN_ID);

        assertThat(found.getDescription()).isEqualTo("Quo vadis?!");
    }

    @Test
    void shouldAddLikesInfoToDto() throws DataException {
        User user = new User();
        user.setExternalId("simple");
        user.setId(OWNER_ID);

        Like like = new Like();
        like.setId(1L);
        like.setOwner(user);

        List<Like> likes = Collections.singletonList(like);
        entity.setLikes(likes);

        when(repository.findById(CAMPAIGN_ID)).thenReturn(Optional.of(entity));
        DonationDto found = service.getCampaignDto(CAMPAIGN_ID);

        assertThat(found.isLiked()).isTrue();
        assertThat(found.getLikesCount()).isEqualTo(1);
    }

    @Test
    void shouldThrowWhenCampaignDoesNotExist() {
        when(repository.findById(CAMPAIGN_ID)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataException.class, () -> service.getCampaignDto(CAMPAIGN_ID));
    }

    @Test
    void shouldGoToRepoToGetAllCampaigns() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));
        service.getAllCampaigns(pageable);

        verify(repository).findAll(pageable);
    }
}
