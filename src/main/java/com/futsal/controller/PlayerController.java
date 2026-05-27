package com.futsal.controller;

import com.futsal.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/players")
    public String getPlayers(@RequestParam(defaultValue = "1") int team,
                             @RequestParam(defaultValue = "yellow-black") String theme,
                             Model model) {
        model.addAttribute("title", "Горизонт - состав команды");
        model.addAttribute("players", playerService.getPlayersByTeam(team));
        model.addAttribute("currentTeam", team);
        model.addAttribute("currentTheme", theme);
        model.addAttribute("content", "players");
        return "layout";
    }

    @GetMapping("/stats")
    public String getStats(@RequestParam(defaultValue = "1") int team,
                           @RequestParam(defaultValue = "yellow-black") String theme,
                           Model model) {
        model.addAttribute("title", "Горизонт - статистика игроков");
        model.addAttribute("players", playerService.getPlayersByTeam(team));
        model.addAttribute("currentTeam", team);
        model.addAttribute("currentTheme", theme);
        model.addAttribute("content", "stats");
        return "layout";
    }
}