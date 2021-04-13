package io.aktivator.user;

import io.aktivator.exceptions.DataException;
import io.aktivator.exceptions.ResourceAlreadyExists;
import io.aktivator.user.model.User;
import io.aktivator.user.services.AuthUserDTO;
import io.aktivator.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    public static final String EXTERNAL_ID = "123456UD";
    public static final String USER_CAUSEA_ORG = "user@causea.org";
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    @Captor
    ArgumentCaptor<AuthUserDTO> dtoCaptor;

    @Test
    public void shouldGoToServiceToUpdateUserInfo() {
        AuthUserDTO authUserDTO = new AuthUserDTO();
        authUserDTO.setEmail(USER_CAUSEA_ORG);
        when(userService.getExternalUserId()).thenReturn(EXTERNAL_ID);
        userController.editUserInfo(authUserDTO);
        verify(userService).updateUserInfo(dtoCaptor.capture(), eq(EXTERNAL_ID));
        assertThat(dtoCaptor.getValue().getEmail()).isEqualTo(USER_CAUSEA_ORG);
    }

    @Test
    public void shouldThrowExceptionIfUserAlreadyRegistered() {
        // From the token it gets the externalID.
        when(userService.getExternalUserId()).thenReturn(EXTERNAL_ID);
        // It needs to fetch the internal user object for that external ID.
        when(userService.getUser(EXTERNAL_ID)).thenReturn(Optional.of(new User()));

        assertThatThrownBy(() -> userController.registerUser())
                .isInstanceOf(ResourceAlreadyExists.class)
                .hasMessage("This user is already registered.");

    }
}