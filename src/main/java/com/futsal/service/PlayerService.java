package com.futsal.service;

import com.futsal.model.Player;
import com.futsal.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    private void validatePlayer(Player player) {
        if (player.getFirstName() == null || player.getFirstName().trim().isEmpty())
            throw new IllegalArgumentException("Имя не может быть пустым");
        if (player.getLastName() == null || player.getLastName().trim().isEmpty())
            throw new IllegalArgumentException("Фамилия не может быть пустой");

        if (player.getBirthDate() == null || player.getBirthDate().trim().isEmpty())
            throw new IllegalArgumentException("Дата рождения обязательна");
        if (!player.getBirthDate().matches("\\d{2}\\.\\d{2}\\.\\d{4}"))
            throw new IllegalArgumentException("Дата рождения должна быть в формате ДД.ММ.ГГГГ");
        try {
            String[] parts = player.getBirthDate().split("\\.");
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            LocalDate birth = LocalDate.of(year, month, day);
            if (birth.isAfter(LocalDate.now()))
                throw new IllegalArgumentException("Дата рождения не может быть в будущем");
        } catch (Exception e) {
            throw new IllegalArgumentException("Некорректная дата рождения");
        }

        if (player.getNumber() < 0 || player.getNumber() > 999)
            throw new IllegalArgumentException("Номер игрока должен быть от 0 до 999");

        List<Player> allPlayers = playerRepository.findAll();
        for (Player p : allPlayers) {
            if (p.getNumber() == player.getNumber() && !p.getId().equals(player.getId())) {
                throw new IllegalArgumentException("Игрок с номером " + player.getNumber() + " уже существует");
            }
        }

        if (player.getPosition() == null || (!player.getPosition().equals("Вратарь") && !player.getPosition().equals("Полевой")))
            throw new IllegalArgumentException("Позиция должна быть 'Вратарь' или 'Полевой'");

        if (player.getTeamType() != 1 && player.getTeamType() != 2)
            throw new IllegalArgumentException("Состав должен быть 1 или 2");

        if (player.getGoals() < 0)
            throw new IllegalArgumentException("Количество голов не может быть отрицательным");
        if (player.getAssists() < 0)
            throw new IllegalArgumentException("Количество ассистов не может быть отрицательным");
        if (player.getYellowCards() < 0)
            throw new IllegalArgumentException("Количество жёлтых карточек не может быть отрицательным");
        if (player.getRedCards() < 0)
            throw new IllegalArgumentException("Количество красных карточек не может быть отрицательным");
        if (player.getGames() < 0)
            throw new IllegalArgumentException("Количество сыгранных матчей не может быть отрицательным");
    }

    public Player savePlayer(Player player) {
        validatePlayer(player);
        return playerRepository.save(player);
    }

    public List<Player> getPlayersByTeam(int teamType) {
        return playerRepository.findByTeamTypeOrderByNumberAsc(teamType);
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAllByOrderByTeamTypeAscNumberAsc();
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    public long count() {
        return playerRepository.count();
    }
}