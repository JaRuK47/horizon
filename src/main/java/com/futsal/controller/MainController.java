package com.futsal.controller;

import com.futsal.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "yellow-black") String theme,
                       Model model) {
        model.addAttribute("title", "Горизонт - история команды");
        model.addAttribute("historyPosts", postService.getHistoryPosts());
        model.addAttribute("currentTheme", theme);
        model.addAttribute("content", "index");
        return "layout";
    }
}