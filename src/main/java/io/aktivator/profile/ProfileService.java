package io.aktivator.profile;

import io.aktivator.model.DataException;
import io.aktivator.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final UserProfileRepository repository;

    @Autowired
    ProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public boolean exists(String userId) {
        return repository.findByUserId(userId).isPresent();
    }

    public UserProfile createProfile(UserDTO user) throws DataException {
        if (exists(user.getId())) {
            throw new DataException("Profile for ID " + user.getId() + " already exists.");
        } else {
            UserProfile userProfile = new UserProfile();
            userProfile.setUserId(user.getId());
            userProfile.setName(user.getName());
            userProfile.setSurname(user.getSurname());
            userProfile.setEmail(user.getEmail());
            userProfile.setUsername(user.getUsername());

            return repository.save(userProfile);
        }
    }

    UserProfile getProfile(String userId) throws DataException {
        return repository.findByUserId(userId).orElseThrow(() -> new DataException("Profile doesn't exist"));
    }
}
