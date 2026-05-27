package com.futsal.repository;

import com.futsal.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamTypeOrderByNumberAsc(int teamType);
    List<Player> findByTeamType(int teamType);
    List<Player> findAllByOrderByTeamTypeAscNumberAsc();
}