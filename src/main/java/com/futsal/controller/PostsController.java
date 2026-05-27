package com.futsal.controller;

import com.futsal.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostsController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts")
    public String getPosts(@RequestParam(defaultValue = "yellow-black") String theme,
                           Model model) {
        model.addAttribute("title", "Горизонт - новости");
        model.addAttribute("newsPosts", postService.getNewsPosts());
        model.addAttribute("currentTheme", theme);
        model.addAttribute("content", "posts");
        return "layout";
    }
}