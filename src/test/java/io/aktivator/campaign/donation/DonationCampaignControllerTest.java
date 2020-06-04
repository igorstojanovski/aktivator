package io.aktivator.campaign.donation;

import io.aktivator.exceptions.DataException;
import io.aktivator.user.model.User;
import io.aktivator.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationCampaignControllerTest {
    public static final Long OWNER_ID = 1L;
    @Mock
    private UserService userService;
    @Mock
    private DonationCampaignService donationCampaignService;
    private DonationCampaignController controller;
    private DonationCampaignDto entity;

    @BeforeEach
    void setupTest() {
        controller = new DonationCampaignController(userService, donationCampaignService);
        Date created = new Date();
        Date startDate = new Date();
        @NotNull Date endDate = convertLocalDateToDate(LocalDate.now().plusDays(30));

        entity = new DonationCampaignDto();
        entity.setTarget(2000L);
        entity.setCreated(created);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setOwnerId(OWNER_ID);
        entity.setDescription("Quo vadis?!");
        entity.setTitle("Latin lessons donation");
    }

    @Test
    void shouldGoToServiceToGetCampaign() throws DataException {
        when(donationCampaignService.getCampaign(123L)).thenReturn(entity);
        ResponseEntity<DonationCampaignDto> response = controller.getCampaign(123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(entity);
    }

    @Test
    void shouldReturnStatus404WhenTheCampaignDoesNotAlreadyExist() throws DataException {
        when(donationCampaignService.getCampaign(123L)).thenThrow(new DataException(""));
        ResponseEntity<DonationCampaignDto> response = controller.getCampaign(123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnTheObjectProvidedByTheRepository() {
        User user = new User();
        user.setId(OWNER_ID);
        when(userService.getCurrentUser()).thenReturn(user);
        DonationCampaignDto request = new DonationCampaignDto();
        DonationCampaign donationCampaign = new DonationCampaign();
        when(donationCampaignService.save(request, OWNER_ID)).thenReturn(donationCampaign);

        ResponseEntity<DonationCampaign> response = controller.createDonationCampaign(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(donationCampaign);
    }

    @Test
    void shouldReturnErrorOnCreateWhenIdIsProvided() {
        DonationCampaignDto request = new DonationCampaignDto();
        request.setId(1L);

        ResponseEntity<DonationCampaign> response = controller.createDonationCampaign(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldUpdateCampaign() {
        User user = new User();
        user.setId(OWNER_ID);
        when(userService.getCurrentUser()).thenReturn(user);
        DonationCampaignDto request = new DonationCampaignDto();
        request.setId(1L);
        DonationCampaign donationCampaign = new DonationCampaign();
        when(donationCampaignService.save(request, OWNER_ID)).thenReturn(donationCampaign);

        ResponseEntity<DonationCampaign> response = controller.updateDonationCampaign(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(donationCampaign);
    }

    @Test
    void shouldReturn404WhenCampaignDoesNotHaveId() {
        DonationCampaignDto request = new DonationCampaignDto();
        ResponseEntity<DonationCampaign> response = controller.updateDonationCampaign(request);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGoToServiceToGetPagedResults() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));
        controller.getAllCampaigns(pageable);

        verify(donationCampaignService).getAllCampaigns(pageable);
    }

    private Date convertLocalDateToDate(LocalDate myLocalDate) {
        return Date.from(myLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }
}
