package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private ExtendedProfileRepository extendedProfileRepository;
    @Mock
    private ExternalUserService externalUserService;
    private ProfileService profileService;

    @BeforeEach
    void setupTest() {
        profileService = new ProfileService(userService, extendedProfileRepository, externalUserService);
    }

    @Test
    void shouldCreateProfileFromRequestingUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("123");
        userDTO.setUsername("dummy123");

        when(userService.getCurrentUser()).thenReturn(userDTO);
        when(extendedProfileRepository.findByOwnerId("123")).thenReturn(Optional.of(new ExtendedProfile()));
        Profile profile = profileService.getProfile();
        assertThat(profile.getUsername()).isEqualTo("dummy123");
    }
}
