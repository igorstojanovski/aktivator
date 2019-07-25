package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static io.aktivator.profile.ProfileType.ACTIVIST;

@Service
class ProfileService {

    private final UserProfileRepository repository;

    @Autowired
    ProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    private boolean exists(String userId) {
        Optional<UserProfile> byUserId = repository.findByUserId(userId);
        return byUserId.isPresent();
    }

    UserProfile createProfile(UserDTO user) throws DataException {
        if (exists(user.getId())) {
            throw new DataException("Profile for ID " + user.getId() + " already exists.");
        } else {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());
            userProfile.setName(user.getName());
            userProfile.setSurname(user.getSurname());
            userProfile.setEmail(user.getEmail());
            userProfile.setUsername(user.getUsername());
            userProfile.setTypes(user.getTypes());

            setInactiveWhenActivist(userProfile);

            return repository.save(userProfile);
        }
    }

    private void setInactiveWhenActivist(UserProfile userProfile) {
        if (userProfile.getTypes().contains(ACTIVIST)) {
            userProfile.setActive(false);
        }
    }

    UserProfile getProfile(String userId) throws DataException {
        return repository.findByUserId(userId).orElseThrow(() -> new DataException("Profile doesn't exist"));
    }

    UserProfile updateProfile(UserProfile editedProfile) {
        return repository.save(editedProfile);
    }
}
