package org.igorski.springkeycloak.services;

import org.igorski.springkeycloak.model.Initiative;
import org.igorski.springkeycloak.repositories.InitiativeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitiativeServiceTest {

    private InitiativeService service;
    @Mock
    private InitiativeRepository initiativeRepository;

    @BeforeEach
    public void before() {
        service = new InitiativeService(initiativeRepository);
    }

    @Test
    public void shouldReturnCorrectInitiative() {
        Initiative initiative = new Initiative();
        initiative.setName("Test Name");
        initiative.setDescription("Some Description");

        when(initiativeRepository.save(initiative)).thenReturn(initiative);

        assertThat(service.createInitiative(initiative)).isEqualTo(initiative);
    }
}