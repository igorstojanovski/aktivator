package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import io.aktivator.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private ProfileService profileService;
    private UserDTO userDTO;
    private UserProfile userProfile;
    private UserProfileController userProfileController;

    @BeforeEach
    public void beforeEach() {
        userProfileController = new UserProfileController(userService, profileService);

        userDTO = new UserDTO();
        userDTO.setEmail("travolta@aktivator.io");
        userDTO.setId("223");
        userDTO.setName("Igor");
        userDTO.setSurname("Stojanovski");
        userDTO.setUsername("igorski");

        when(userService.getCurrentUser()).thenReturn(userDTO);

        userProfile = new UserProfile();
        userProfile.setName("Igor");
        userProfile.setSurname("Stojanovski");
        userProfile.setUsername("igorski");
        userProfile.setId(1L);
        userProfile.setUserId("223");
        userProfile.setEmail("travolta@aktivator.io");
    }

    @Test
    void shouldReturnProfileWhenItExists() throws DataException {
        when(profileService.getProfile("223")).thenReturn(userProfile);
        ResponseEntity<UserProfile> response = userProfileController.getCurrentProfile();
        UserProfile profile = response.getBody();

        assertThat(profile).isEqualTo(userProfile);
    }

    @Test
    void shouldThrowIfProfileDoesNotExist() throws DataException {
        when(profileService.getProfile("223")).thenThrow(new DataException("Profile not found."));
        ResponseEntity<UserProfile> response = userProfileController.getCurrentProfile();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldThrowWhenProfileAlreadyExistsAtCreation() throws DataException {
        when(profileService.createProfile(userDTO)).thenThrow(new DataException("Profile already exists."));
        ResponseEntity<UserProfile> response = userProfileController.createUserProfile();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void shouldReturnCreatedProfileAtCreation() throws DataException {
        when(profileService.createProfile(userDTO)).thenReturn(userProfile);
        ResponseEntity<UserProfile> response = userProfileController.createUserProfile();
        UserProfile profile = response.getBody();

        assertThat(profile).isEqualTo(userProfile);
    }

    @Test
    void shouldUpdateWithGivenDataAtUpdate() throws DataException {

        UserProfileUpdateRequest updateRequest = new UserProfileUpdateRequest();
        updateRequest.setName("John");
        updateRequest.setSurname("Doe");
        updateRequest.setUsername("JohnnyBravo");

        UserProfile editedProfile = new UserProfile();
        editedProfile.setName("John");
        editedProfile.setSurname("Doe");
        editedProfile.setUsername("JohnnyBravo");
        editedProfile.setId(1L);
        editedProfile.setUserId("223");
        editedProfile.setEmail("travolta@aktivator.io");

        when(profileService.getProfile(userDTO.getId())).thenReturn(userProfile);
        when(profileService.updateProfile(editedProfile)).thenReturn(editedProfile);

        ResponseEntity<UserProfile> response = userProfileController.updateUserProfile(updateRequest);
        assertThat(response.getBody()).isEqualTo(userProfile);
    }
}
