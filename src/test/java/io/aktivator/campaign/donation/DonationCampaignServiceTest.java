package io.aktivator.campaign.donation;

import io.aktivator.exceptions.DataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
    private DonationCampaign entity;
    private DonationCampaignDto createRequest;
    @Captor
    private ArgumentCaptor<DonationCampaign> entityArgumentCaptor = ArgumentCaptor.forClass(DonationCampaign.class);

    @BeforeEach
    void setupTest() {
        service = new DonationCampaignService(repository);
        Date created = new Date();
        Date startDate = new Date();
        Date endDate = convertLocalDateToDate(LocalDate.now().plusDays(30));

        entity = new DonationCampaign();
        entity.setTarget(2000L);
        entity.setCreated(created);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setOwner("23456099");
        entity.setDescription("Quo vadis?!");
        entity.setTitle("Latin lessons donation");

        createRequest = new DonationCampaignDto();
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
        service.save(createRequest, "23456099");
        verify(repository).save(entityArgumentCaptor.capture());

        DonationCampaign value = entityArgumentCaptor.getValue();
        assertThat(value.getTarget()).isEqualTo(2000L);
    }

    @Test
    void shouldGoToRepoToGetById() throws DataException {
        when(repository.findById(123L)).thenReturn(Optional.of(entity));
        DonationCampaign found = service.getCampaign(123L);

        assertThat(found).isEqualTo(entity);
    }

    @Test
    void shouldThrowWhenCampaignDoesNotExist() {
        when(repository.findById(123L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataException.class, () -> service.getCampaign(123L));
    }

    @Test
    void shouldGoToRepoToGetAllCampaigns() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));
        service.getAllCampaigns(pageable);

        verify(repository).findAll(pageable);
    }
}
