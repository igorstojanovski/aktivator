package io.aktivator.campaign.donation;

import io.aktivator.model.DataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationCampaignServiceTest {

    @Mock
    private DonationCampaignRepository repository;
    private DonationCampaignService service;
    private DonationCampaignEntity entity;
    private DonationCampaignCreateRequest createRequest;
    private Date created;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setupTest() {
        service = new DonationCampaignService(repository);
        created = new Date();
        startDate = new Date();
        endDate = convertLocalDateToDate(LocalDate.now().plusDays(30));

        entity = new DonationCampaignEntity();
        entity.setTarget(2000L);
        entity.setCreated(created);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setOwnerId("23456099");
        entity.setDescription("Quo vadis?!");
        entity.setTitle("Latin lessons donation");

        createRequest = new DonationCampaignCreateRequest();
        createRequest.setTarget(2000L);
        createRequest.setCreated(created);
        createRequest.setStartDate(startDate);
        createRequest.setEndDate(endDate);
        createRequest.setOwnerId("23456099");
        createRequest.setDescription("Quo vadis?!");
        createRequest.setTitle("Latin lessons donation");
    }

    private Date convertLocalDateToDate(LocalDate myLocalDate) {
        return Date.from(myLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    @Test
    void shouldGoToRepoToSave() {
        service.save(createRequest);
        verify(repository).save(entity);
    }

    @Test
    void shouldGoToRepoToGetById() throws DataException {
        when(repository.findById(123L)).thenReturn(Optional.of(entity));
        DonationCampaignEntity found = service.getCampaign(123L);

        assertThat(found).isEqualTo(entity);
    }

    @Test
    void shouldThrowWhenCampaignDoesNotExist() {
        when(repository.findById(123L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataException.class, () -> service.getCampaign(123L));
    }
}