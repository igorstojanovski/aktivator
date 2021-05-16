package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;
import io.aktivator.exceptions.ResourceAlreadyExists;
import io.aktivator.user.exceptions.UserNotRegisteredException;
import io.aktivator.user.model.User;
import io.aktivator.user.model.UserInformation;
import io.aktivator.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final String EXTERNAL_USER_ID = "12user";
    public static final String DUMMY_EMAIL_COM = "dummy@email.com";
    public static final String USER_ADDRESS = "A very long address.";
    public static final String SHERLOCK = "Sherlock";
    public static final String HOLMES = "Holmes";
    private static User user;
    @Captor
    private ArgumentCaptor<User> userCaptor;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationServiceClient authClient;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setExternalId(EXTERNAL_USER_ID);
        user.setId(1L);
        user.setUserInformation(getUserInformationStub());
    }

    private UserInformation getUserInformationStub() {
        UserInformation userInformation = new UserInformation();
        userInformation.setLongAddress(USER_ADDRESS);
        userInformation.setName(SHERLOCK);
        userInformation.setSurname(HOLMES);
        return userInformation;
    }

    @Test
    public void shouldReturnUserFromRepository() {
        when(userRepository.findUserByExternalId(EXTERNAL_USER_ID)).thenReturn(Optional.of(user));
        Optional<User> returnedUser = userService.getUser(EXTERNAL_USER_ID);
        assertThat(returnedUser).isNotEmpty();
        assertThat(returnedUser.get()).isEqualTo(user);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUserDoesNotExist() {
        when(userRepository.findUserByExternalId(EXTERNAL_USER_ID)).thenReturn(Optional.empty());

        Optional<User> returnedUser = userService.getUser(EXTERNAL_USER_ID);
        assertThat(returnedUser).isEmpty();
    }

    @Test
    public void shouldReturn404WhenTokenUserDoesNotExist() {
        assertThatThrownBy(() -> userService.getCurrentUser()).isInstanceOf(UserNotRegisteredException.class);
    }

    @Test
    public void shouldGetUserByInternalId() {
        // The information returned is a combination of information from the identity manager plus
        // information from the internal database.
        // First, the internal user needs to be retrieved based on the internal id.
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        AuthUserDTO auth0User = new AuthUserDTO();
        auth0User.setEmail(DUMMY_EMAIL_COM);
        auth0User.setName("John");
        auth0User.setSurname("Watson");
        // From that object, the external ID is going to be taken.
        when(authClient.getUserByExternalId(user.getExternalId())).thenReturn(auth0User);

        AuthUserDTO authUserDTO = userService.getInformationInternal(1L);

        assertThat(authUserDTO.getEmail()).isEqualTo(DUMMY_EMAIL_COM);
        assertThat(authUserDTO.getLongAddress()).isEqualTo(USER_ADDRESS);
        assertThat(authUserDTO.getName()).isEqualTo(SHERLOCK);
        assertThat(authUserDTO.getSurname()).isEqualTo(HOLMES);
    }

    @Test
    public void shouldUpdateUserInformationInRepository() {
        UserInformation userInformation = new UserInformation();
        userInformation.setLongAddress("street, number, city, post code and country");
        user.setUserInformation(userInformation);
        when(userRepository.findUserByExternalId(user.getExternalId())).thenReturn(Optional.of(user));

        userService.updateUserInfo(getAuthUserDTO(), user.getExternalId());

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
        assertThat(userCaptor.getValue().getUserInformation()).isEqualTo(userInformation);
    }

    private AuthUserDTO getAuthUserDTO() {
        AuthUserDTO dto = new AuthUserDTO();
        dto.setEmail("user@causea.org");
        dto.setName("Causea");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("keyOne", "valueOne");
        dto.setMetadata(metadata);
        return dto;
    }

    @Test
    public void shouldThrowExceptionIfUserAlreadyRegistered() {
        // It needs to fetch the internal user object for that external ID.
        when(userRepository.findUserByExternalId(EXTERNAL_USER_ID)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userService.registerUser(EXTERNAL_USER_ID))
                .isInstanceOf(ResourceAlreadyExists.class)
                .hasMessage("This user is already registered.");

    }

}