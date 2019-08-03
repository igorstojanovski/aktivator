package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserService userService;
    private ProfileService profileService;

    @BeforeEach
    void setupTest() {
        profileService = new ProfileService(userService);
    }

    @Test
    void shouldCreateProfileFromRequestingUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("dummy123");

        when(userService.getCurrentUser()).thenReturn(userDTO);

        Profile profile = profileService.getProfile();
        assertThat(profile.getUsername()).isEqualTo("dummy123");
    }
}
