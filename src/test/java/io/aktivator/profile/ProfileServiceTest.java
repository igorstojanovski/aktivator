package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
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
    private UserDTO userDTO;
    @Captor
    private ArgumentCaptor<ProfileUpdateRequest> profileUpdateCaptor
            = ArgumentCaptor.forClass(ProfileUpdateRequest.class);
    @Captor
    private ArgumentCaptor<ExtendedProfile> extendedProfileCaptor
            = ArgumentCaptor.forClass(ExtendedProfile.class);

    @BeforeEach
    void setupTest() {
        profileService = new ProfileService(userService, extendedProfileRepository, externalUserService);

        userDTO = new UserDTO();
        userDTO.setName("Johnny");
        userDTO.setSurname("Bravo");
        userDTO.setEmail("johnny.bravo@gmail.com");
        userDTO.setUsername("bravo");
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

    @Test
    void shouldCombineProfilesAndRemoveSensitiveData() {

        ExtendedProfile extendedProfile = new ExtendedProfile();
        extendedProfile.setOwnerId("123");
        extendedProfile.setStory("Oh momma!");

        when(extendedProfileRepository.findByOwnerId("123")).thenReturn(Optional.of(extendedProfile));
        when(externalUserService.getUser("123")).thenReturn(userDTO);

        Profile profile = profileService.getPublicProfile("123");
        assertThat(profile.getExtendedProfile()).isNotNull();
        assertThat(profile.getExtendedProfile().getOwnerId()).isEmpty();
        assertThat(profile.getExtendedProfile().getStory()).isEqualTo("Oh momma!");
        assertThat(profile.getEmail()).isEmpty();
        assertThat(profile.getUsername()).isEqualTo("bravo");
    }

    @Test
    void shouldUpdateBothProfileInfoSources() {
        ProfileUpdateRequest profileUpdateRequest = new ProfileUpdateRequest();
        profileUpdateRequest.setFirstName("Johnny");
        profileUpdateRequest.setLastName("Bravo");
        profileUpdateRequest.setStory("Oh momma!");

        when(extendedProfileRepository.findByOwnerId(userDTO.getId())).thenReturn(Optional.empty());
        when(userService.getCurrentUser()).thenReturn(userDTO);
        profileService.updateProfile(profileUpdateRequest);

        verify(externalUserService).editUser(profileUpdateRequest, userDTO.getId());
        verify(extendedProfileRepository).save(extendedProfileCaptor.capture());

        ExtendedProfile extendedProfile = extendedProfileCaptor.getValue();
        assertThat(extendedProfile.getStory()).isEqualTo("Oh momma!");
    }
}
