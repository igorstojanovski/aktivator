package io.aktivator.services;

import io.aktivator.model.DataException;
import io.aktivator.model.Initiative;
import io.aktivator.repositories.InitiativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitiativeService {

    private InitiativeRepository initiativeRepository;

    @Autowired
    public InitiativeService(InitiativeRepository initiativeRepository) {
        this.initiativeRepository = initiativeRepository;
    }

    public Initiative createInitiative(Initiative initiative) {
        return initiativeRepository.save(initiative);
    }

    public Initiative getInitiative(Long initiativeId) throws DataException {
        return initiativeRepository.findById(initiativeId).orElseThrow(() -> new DataException("No Initiative found."));
    }
}
