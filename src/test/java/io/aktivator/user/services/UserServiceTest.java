package io.aktivator.user.services;

import com.auth0.exception.Auth0Exception;
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
    }

    @Test
    public void shouldReturnUserFromRepository() {

        when(userRepository.findUserByExternalId(EXTERNAL_USER_ID)).thenReturn(Optional.of(user));
        Optional<User> returnedUser = userService.getUser(EXTERNAL_USER_ID);
        assertThat(returnedUser).isNotEmpty();
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
        when(userRepository.findUserById(1L)).thenReturn(Optional.of(user));
        AuthUserDTO auth0User = new AuthUserDTO();
        auth0User.setEmail(DUMMY_EMAIL_COM);
        when(authClient.getUserByExternalId(user.getExternalId())).thenReturn(auth0User);

        AuthUserDTO authUserDTO = userService.getUserInfo(1L);
        assertThat(authUserDTO.getEmail()).isEqualTo(DUMMY_EMAIL_COM);
    }

    @Test
    public void shouldUpdateUserInAuth0() throws Auth0Exception {
        AuthUserDTO dto = getAuthUserDTO();
        when(userRepository.findUserByExternalId(user.getExternalId())).thenReturn(Optional.of(user));

        userService.updateUserInfo(dto);

        verify(authClient).updateUserInfo(dto);
    }

    @Test
    public void shouldUpdateUserInformationInRepository() {
        UserInformation userInformation = new UserInformation();
        userInformation.setLongAddress("street, number, city, post code and country");
        user.setUserInformation(userInformation);
        when(userRepository.findUserByExternalId(user.getExternalId())).thenReturn(Optional.of(user));

        userService.updateUserInfo(getAuthUserDTO());

        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue()).isEqualTo(user);
        assertThat(userCaptor.getValue().getUserInformation()).isEqualTo(userInformation);
    }

    private AuthUserDTO getAuthUserDTO() {
        AuthUserDTO dto = new AuthUserDTO();
        dto.setExternalId("12user");
        dto.setEmail("user@causea.org");
        dto.setName("Causea");
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("keyOne", "valueOne");
        dto.setMetadata(metadata);
        return dto;
    }

}