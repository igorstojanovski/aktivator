package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;
    private ProfileService profileService;
    private UserDTO user;
    @Captor
    private ArgumentCaptor<UserProfile> userProfileCaptor = ArgumentCaptor.forClass(UserProfile.class);

    @BeforeEach
    void beforeEach() {
        profileService = new ProfileService(userProfileRepository);
        user = new UserDTO();
        user.setId("223");
        user.setName("Igor");
        user.setSurname("Stojanovski");
        user.setEmail("igorce@gmail.com");
        user.setUsername("igorski");
    }

    @Test
    void shouldThrowWhenProfileAlreadyExists() {
        when(userProfileRepository.findByUserId("223")).thenReturn(Optional.of(new UserProfile()));
        assertThrows(DataException.class, () -> profileService.createProfile(user));
    }

    @Test
    void shouldSaveCorrectObject() throws DataException {
        when(userProfileRepository.findByUserId("223")).thenReturn(Optional.empty());
        when(userProfileRepository.save(ArgumentMatchers.any(UserProfile.class))).thenReturn(new UserProfile());

        assertThat(profileService.createProfile(user)).isNotNull();
        verify(userProfileRepository).save(userProfileCaptor.capture());

        UserProfile profile = userProfileCaptor.getValue();
        assertThat(profile.getEmail()).isEqualTo(user.getEmail());
        assertThat(profile.getName()).isEqualTo(user.getName());
        assertThat(profile.getSurname()).isEqualTo(user.getSurname());
        assertThat(profile.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    void shouldThrowWhenUserExists() {
        when(userProfileRepository.findByUserId(user.getId())).thenReturn(Optional.of(new UserProfile()));
        assertThrows(DataException.class, () -> profileService.createProfile(user));
    }

    @Test
    void shouldUpdateProfile() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("dummy");

        UserProfile savedProfile = new UserProfile();
        savedProfile.setUsername("dummySaved");

        when(userProfileRepository.save(userProfile)).thenReturn(savedProfile);

        UserProfile profile = profileService.updateProfile(userProfile);
        assertThat(profile.getUsername()).isEqualTo(savedProfile.getUsername());
    }

    @Test
    void shouldReturnProfileForUserId() throws DataException {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername("dummy");

        when(userProfileRepository.findByUserId("123")).thenReturn(Optional.of(userProfile));
        UserProfile profile = profileService.getProfile("123");

        assertThat(profile.getUsername()).isEqualTo(userProfile.getUsername());
    }

    @Test
    void shouldThrowWhenUserProfileForUserIdDoesNotExist() throws DataException {
        when(userProfileRepository.findByUserId("123")).thenReturn(Optional.empty());

        assertThrows(DataException.class, () -> profileService.getProfile("123"));
    }
}
