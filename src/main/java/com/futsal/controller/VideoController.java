package com.futsal.controller;

import com.futsal.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VideoController {

    @Autowired
    private PostService postService;

    @GetMapping("/videos")
    public String getVideos(@RequestParam(defaultValue = "1") int team,
                            @RequestParam(defaultValue = "yellow-black") String theme,
                            Model model) {
        model.addAttribute("title", "Горизонт - видео");
        model.addAttribute("videos", postService.getVideos());
        model.addAttribute("currentTeam", team);
        model.addAttribute("currentTheme", theme);
        model.addAttribute("content", "videos");
        return "layout";
    }
}