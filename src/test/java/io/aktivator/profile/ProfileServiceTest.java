package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        Mockito.when(userProfileRepository.findByUserId("223")).thenReturn(Optional.of(new UserProfile()));
        Assertions.assertThrows(DataException.class, () -> profileService.createProfile(user));
    }

    @Test
    void shouldSaveCorrectObject() throws DataException {
        Mockito.when(userProfileRepository.findByUserId("223")).thenReturn(Optional.empty());
        Mockito.when(userProfileRepository.save(ArgumentMatchers.any(UserProfile.class))).thenReturn(new UserProfile());

        assertThat(profileService.createProfile(user)).isNotNull();
        Mockito.verify(userProfileRepository).save(userProfileCaptor.capture());

        UserProfile profile = userProfileCaptor.getValue();
        assertThat(profile.getEmail()).isEqualTo(user.getEmail());
        assertThat(profile.getName()).isEqualTo(user.getName());
        assertThat(profile.getSurname()).isEqualTo(user.getSurname());
        assertThat(profile.getUsername()).isEqualTo(user.getUsername());
    }
}
