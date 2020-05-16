package io.aktivator.campaign.donation;

import io.aktivator.model.DataException;
import io.aktivator.profile.Roles;
import io.aktivator.user.model.UserDTO;
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
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DonationCampaignControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private DonationCampaignService donationCampaignService;
    private DonationCampaignController controller;
    private DonationCampaignEntity entity;

    @BeforeEach
    void setupTest() {
        controller = new DonationCampaignController(userService, donationCampaignService);
        Date created = new Date();
        Date startDate = new Date();
        @NotNull Date endDate = convertLocalDateToDate(LocalDate.now().plusDays(30));

        entity = new DonationCampaignEntity();
        entity.setTarget(2000L);
        entity.setCreated(created);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setOwnerId("23456099");
        entity.setDescription("Quo vadis?!");
        entity.setTitle("Latin lessons donation");
    }

    @Test
    void shouldGoToServiceToGetCampaign() throws DataException {
        when(donationCampaignService.getCampaign(123L)).thenReturn(entity);
        ResponseEntity<DonationCampaignEntity> response = controller.getCampaign(123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(entity);
    }

    @Test
    void shouldReturn404WhenCampaignDoesNotExist() throws DataException {
        when(donationCampaignService.getCampaign(123L)).thenThrow(new DataException(""));
        ResponseEntity<DonationCampaignEntity> response = controller.getCampaign(123L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturn401WhenCreatingUserIsNotActivist() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAuthorities(Collections.emptyList());
        when(userService.getCurrentUser()).thenReturn(userDTO);
        ResponseEntity<DonationCampaignEntity> response = controller.createDonationCampaign(new DonationCampaignCreateRequest());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void shouldReturnTheObjectProvidedByTheRepository() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAuthorities(Collections.singletonList(Roles.ACTIVIST.name()));
        userDTO.setId("23456099");
        when(userService.getCurrentUser()).thenReturn(userDTO);
        DonationCampaignCreateRequest request = new DonationCampaignCreateRequest();
        when(donationCampaignService.save(request, "23456099")).thenReturn(entity);

        ResponseEntity<DonationCampaignEntity> response = controller.createDonationCampaign(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(entity);
    }

    @Test
    void shouldGoTosServiceToGetPagedResults() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("id")));
        controller.getAllCampaigns(pageable);

        verify(donationCampaignService).getAllCampaigns(pageable);
    }

    private Date convertLocalDateToDate(LocalDate myLocalDate) {
        return Date.from(myLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }
}
