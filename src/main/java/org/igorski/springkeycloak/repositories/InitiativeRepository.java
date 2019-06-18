package org.igorski.springkeycloak.repositories;

import org.igorski.springkeycloak.model.Initiative;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InitiativeRepository extends CrudRepository<Initiative, Long> {
}
