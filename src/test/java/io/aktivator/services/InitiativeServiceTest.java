package io.aktivator.services;

import io.aktivator.model.DataException;
import io.aktivator.model.Initiative;
import io.aktivator.repositories.InitiativeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InitiativeServiceTest {

    private InitiativeService service;
    @Mock
    private InitiativeRepository initiativeRepository;
    private Initiative initiative;

    @BeforeEach
    public void before() {
        service = new InitiativeService(initiativeRepository);
        initiative = new Initiative();
        initiative.setId(1L);
        initiative.setName("Test Name");
        initiative.setDescription("Some Description");

    }

    @Test
    public void shouldReturnCorrectInitiative() {
        when(initiativeRepository.save(initiative)).thenReturn(initiative);
        assertThat(service.createInitiative(initiative)).isEqualTo(initiative);
    }

    @Test
    public void shouldReturnInitiativeFoundById() throws DataException {
        when(initiativeRepository.findById(1L)).thenReturn(Optional.of(initiative));
        assertThat(service.getInitiative(1L)).isEqualTo(initiative);
    }

    @Test
    public void shouldThrowDataExceptionWhenNoIdIsFound() {
        when(initiativeRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(DataException.class, () -> service.getInitiative(1L));
    }
}
