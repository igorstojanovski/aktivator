package io.aktivator.user;

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

import static org.assertj.core.api.Assertions.assertThat;
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
        userController.editUserInfo(authUserDTO);
        verify(userService).updateUserInfo(dtoCaptor.capture());
        assertThat(dtoCaptor.getValue().getExternalId()).isNull();
        assertThat(dtoCaptor.getValue().getEmail()).isEqualTo(USER_CAUSEA_ORG);
    }

}