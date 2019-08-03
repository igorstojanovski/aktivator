package io.aktivator.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private ProfileService profileService;
    private ProfileController profileController;

    @BeforeEach
    public void setupTest() {
        profileController = new ProfileController(profileService);
    }

    @Test
    public void shouldGoToServiceToGetProfile() {
        Profile profile = new Profile();
        profile.setUsername("123");

        when(profileService.getProfile()).thenReturn(profile);

        ResponseEntity<Profile> response = profileController.getCurrentUserInfo();

        assertThat(response.getBody()).isEqualTo(profile);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

}
