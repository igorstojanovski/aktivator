package io.aktivator.profile;

import io.aktivator.model.UserDTO;
import io.aktivator.profile.requests.ProfileUpdateRequest;

public interface ExternalUserService {
    void editUser(ProfileUpdateRequest profileUpdateRequest, String ownerId);

    UserDTO getUser(String ownerId);
}
