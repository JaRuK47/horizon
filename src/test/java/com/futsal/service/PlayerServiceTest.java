package com.futsal.service;

import com.futsal.model.Player;
import com.futsal.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Иван", "Иванов", 7, "Полевой", 1);
        player.setId(1L);
        player.setBirthDate("01.01.2000");
    }

    @Test
    void savePlayer_ShouldThrow_WhenNumberAlreadyExistsGlobally() {
        Player existing = new Player("Пётр", "Петров", 7, "Полевой", 1);
        existing.setId(2L);
        existing.setBirthDate("01.01.1999");
        when(playerRepository.findAll()).thenReturn(List.of(existing));

        Player newPlayer = new Player("Иван", "Иванов", 7, "Полевой", 1);
        newPlayer.setBirthDate("01.01.2000");

        Exception exception = assertThrows(RuntimeException.class, () -> playerService.savePlayer(newPlayer));
        assertEquals("Игрок с номером 7 уже существует", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_ShouldThrow_WhenNumberOutOfRange() {
        player.setNumber(1000);
        Exception exception = assertThrows(RuntimeException.class, () -> playerService.savePlayer(player));
        assertEquals("Номер игрока должен быть от 0 до 999", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void savePlayer_ShouldThrow_WhenBirthDateInvalid() {
        player.setBirthDate("31.02.2000");
        Exception exception = assertThrows(RuntimeException.class, () -> playerService.savePlayer(player));
        assertTrue(exception.getMessage().contains("Некорректная дата рождения"));
    }

    @Test
    void savePlayer_ShouldSave_WhenAllValid() {
        when(playerRepository.findAll()).thenReturn(List.of());
        when(playerRepository.save(any(Player.class))).thenReturn(player);
        Player saved = playerService.savePlayer(player);
        assertNotNull(saved);
        assertEquals(7, saved.getNumber());
        verify(playerRepository, times(1)).save(player);
    }
}