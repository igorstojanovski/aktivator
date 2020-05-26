package io.aktivator.user.services;

import io.aktivator.user.exceptions.UserNotRegisteredException;
import io.aktivator.user.model.User;
import io.aktivator.user.repository.UserRepository;
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
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    public void shouldReturnUserFromRepository() {
        User user = new User();
        user.setExternalId(EXTERNAL_USER_ID);
        user.setId(1L);

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
}