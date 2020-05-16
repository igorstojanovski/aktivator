package io.aktivator.profile;

import io.aktivator.profile.requests.ProfileUpdateRequest;
import io.aktivator.user.model.UserDTO;

public interface ExternalUserService {
    void editUser(ProfileUpdateRequest profileUpdateRequest, String ownerId);

    UserDTO getUser(String ownerId);
}
