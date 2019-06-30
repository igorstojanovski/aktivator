package org.igorski.springkeycloak.repositories;

import org.igorski.springkeycloak.model.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {
    Optional<UserProfile> findByUserId(String userId);
}
