package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.profile.entities.ExtendedProfile;
import io.aktivator.profile.entities.Profile;
import io.aktivator.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProfileControllerIT {

    @MockBean
    private ExtendedProfileRepository extendedProfileRepository;
    @MockBean
    @Qualifier("userService")
    private UserService userService;
    @Autowired
    private ProfileController profileController;

    @Test
    void shouldUseService() {
        ExtendedProfile extendedProfile = new ExtendedProfile();
        extendedProfile.setId(123456L);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("bravo");
        userDTO.setId("123456t");

        when(userService.getCurrentUser()).thenReturn(userDTO);

        when(extendedProfileRepository.save(any(ExtendedProfile.class))).thenReturn(extendedProfile);
        ResponseEntity<Profile> response = profileController.createProfile();

        Profile body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getUsername()).isEqualTo("bravo");
        assertThat(body.getExtendedProfile().getId()).isEqualTo(123456L);
    }
}
