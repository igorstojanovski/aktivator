package io.aktivator.user.services;

import io.aktivator.user.exceptions.UserNotRegisteredException;
import io.aktivator.user.model.User;
import io.aktivator.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    public static final String EXTERNAL_USER_ID = "12user";
    public static final String DUMMY_EMAIL_COM = "dummy@email.com";
    private static User user;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationServiceClient authClient;
    @InjectMocks
    private UserService userService;

    @BeforeAll
    public static void once() {
        user = getUser();
    }

    private static User getUser() {
        User user = new User();
        user.setExternalId(EXTERNAL_USER_ID);
        user.setId(1L);
        return user;
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

}