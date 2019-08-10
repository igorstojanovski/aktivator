package io.aktivator.profile;

import io.aktivator.model.UserDTO;

public interface ExternalUserService {
    void editUser(ProfileUpdateRequest profileUpdateRequest, String ownerId);

    UserDTO getUser(String ownerId);
}
