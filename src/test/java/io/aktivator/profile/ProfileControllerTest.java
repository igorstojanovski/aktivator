package io.aktivator.profile;

import io.aktivator.profile.entities.Profile;
import io.aktivator.profile.requests.ProfileUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    private static final String JOHNNY_BRAVO = "Johnny Bravo";
    @Mock
    private ProfileService profileService;
    @Mock
    private ExternalUserService externalUserService;
    private ProfileController profileController;
    @Captor
    private ArgumentCaptor<ProfileUpdateRequest> updateRequestArgumentCaptor
            = ArgumentCaptor.forClass(ProfileUpdateRequest.class);
    @BeforeEach
    void setupTest() {
        profileController = new ProfileController(profileService, externalUserService);
    }

    @Test
    void shouldGoToServiceToGetProfile() {
        Profile profile = new Profile();
        profile.setUsername("123");

        when(profileService.getProfile()).thenReturn(profile);

        ResponseEntity<Profile> response = profileController.getCurrentUserInfo();

        assertThat(response.getBody()).isEqualTo(profile);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldGoToProfileServiceToGetPublicProfile() {
        Profile profile = new Profile();
        profile.setName(JOHNNY_BRAVO);

        when(profileService.getPublicProfile("123")).thenReturn(profile);
        ResponseEntity<Profile> response = profileController.getPublicProfile("123");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(JOHNNY_BRAVO);
    }

    @Test
    void shouldCallUpdateServiceWithProvidedRequestObject() {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        profileUpdateRequest.setFirstName(JOHNNY_BRAVO);

        profileController.editCurrentUserInfo(profileUpdateRequest);
        verify(profileService).updateProfile(updateRequestArgumentCaptor.capture());
        assertThat(updateRequestArgumentCaptor.getValue().getFirstName()).isEqualTo(JOHNNY_BRAVO);
    }

    @Test
    public void shouldCallService() {
        Profile profile = new Profile();
        profile.setName("Johnny");
        when(profileService.createProfile()).thenReturn(profile);

        assertThat(profileController.createProfile().getBody().getName()).isEqualTo("Johnny");
    }
}
