package io.aktivator.user;

import io.aktivator.user.services.UserDto;
import io.aktivator.user.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.amazonaws.auth.profile.internal.ProfileKeyConstants.EXTERNAL_ID;
import static io.aktivator.user.UserTestHelper.DUMMY_EMAIL_COM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
  private static final UserTestHelper USER_HELPER = new UserTestHelper();
  @Mock private UserService userService;
  @InjectMocks private UserController userController;
  @Captor ArgumentCaptor<UserDto> dtoCaptor;

  @Test
  public void shouldGoToServiceToUpdateUserInfo() {

    when(userService.getExternalUserId()).thenReturn(EXTERNAL_ID);
    userController.editUserInfo(USER_HELPER.getAuthUserDTO());
    verify(userService).updateUserInfo(dtoCaptor.capture(), eq(EXTERNAL_ID));
    assertThat(dtoCaptor.getValue().getEmail()).isEqualTo(DUMMY_EMAIL_COM);
  }
}
