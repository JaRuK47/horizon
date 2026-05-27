package com.futsal.controller;

import com.futsal.model.Player;
import com.futsal.model.Post;
import com.futsal.service.PlayerService;
import com.futsal.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PostService postService;

    @GetMapping("/login")
    public String login() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("playersCount", playerService.count());
        model.addAttribute("newsCount", postService.getPostsByCategory("news").size());
        model.addAttribute("videosCount", postService.getPostsByCategory("video").size());
        model.addAttribute("historyCount", postService.getPostsByCategory("history").size());
        model.addAttribute("content", "admin/dashboard");
        return "layout";
    }

    @GetMapping("/players")
    public String listPlayers(Model model) {
        model.addAttribute("players", playerService.getAllPlayers());
        model.addAttribute("content", "admin/players-list");
        return "layout";
    }

    @GetMapping("/players/add")
    public String addPlayerForm(Model model) {
        model.addAttribute("player", new Player());
        model.addAttribute("content", "admin/player-form");
        return "layout";
    }

    @PostMapping("/players/save")
    public String savePlayer(@ModelAttribute Player player,
                             @RequestParam(value = "photoYellowFile", required = false) MultipartFile photoYellowFile,
                             @RequestParam(value = "photoWhiteBlackFile", required = false) MultipartFile photoWhiteBlackFile,
                             @RequestParam(value = "photoBlackWhiteFile", required = false) MultipartFile photoBlackWhiteFile,
                             Model model) {
        try {
            if (photoYellowFile != null && !photoYellowFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_yellow_" + photoYellowFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/players/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(photoYellowFile.getInputStream(), uploadPath.resolve(fileName));
                player.setPhotoYellow("/uploads/players/" + fileName);
            }

            if (photoWhiteBlackFile != null && !photoWhiteBlackFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_whiteblack_" + photoWhiteBlackFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/players/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(photoWhiteBlackFile.getInputStream(), uploadPath.resolve(fileName));
                player.setPhotoWhiteBlack("/uploads/players/" + fileName);
            }

            if (photoBlackWhiteFile != null && !photoBlackWhiteFile.isEmpty()) {
                String fileName = System.currentTimeMillis() + "_blackwhite_" + photoBlackWhiteFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/players/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(photoBlackWhiteFile.getInputStream(), uploadPath.resolve(fileName));
                player.setPhotoBlackWhite("/uploads/players/" + fileName);
            }

            playerService.savePlayer(player);
            return "redirect:/admin/players";
        } catch (IllegalArgumentException e) {

            Map<String, String> fieldErrors = new HashMap<>();
            String msg = e.getMessage().toLowerCase();
            String originalMsg = e.getMessage();

            if (msg.contains("имя") || msg.contains("firstname"))
                fieldErrors.put("firstName", originalMsg);
            else if (msg.contains("фамилия") || msg.contains("lastname"))
                fieldErrors.put("lastName", originalMsg);
            else if (msg.contains("номер") || msg.contains("игрок с номером"))
                fieldErrors.put("number", originalMsg);
            else if (msg.contains("позиция"))
                fieldErrors.put("position", originalMsg);
            else if (msg.contains("состав"))
                fieldErrors.put("teamType", originalMsg);
            else if (msg.contains("дата рождения") || msg.contains("формат") || msg.contains("дд.мм.гггг"))
                fieldErrors.put("birthDate", originalMsg);
            else if (msg.contains("гол") || msg.contains("goals"))
                fieldErrors.put("goals", originalMsg);
            else if (msg.contains("ассист") || msg.contains("assists"))
                fieldErrors.put("assists", originalMsg);
            else if (msg.contains("матч") || msg.contains("games"))
                fieldErrors.put("games", originalMsg);
            else if (msg.contains("жёлт") || msg.contains("yellow"))
                fieldErrors.put("yellowCards", originalMsg);
            else if (msg.contains("красн") || msg.contains("red"))
                fieldErrors.put("redCards", originalMsg);
            else
                fieldErrors.put("general", originalMsg);

            model.addAttribute("player", player);
            model.addAttribute("fieldErrors", fieldErrors);
            model.addAttribute("content", "admin/player-form");
            return "layout";
        } catch (Exception e) {
            model.addAttribute("player", player);
            model.addAttribute("error", "Ошибка сохранения: " + e.getMessage());
            model.addAttribute("content", "admin/player-form");
            return "layout";
        }
    }

    @GetMapping("/players/edit/{id}")
    public String editPlayerForm(@PathVariable Long id, Model model) {
        model.addAttribute("player", playerService.getPlayerById(id));
        model.addAttribute("content", "admin/player-form");
        return "layout";
    }

    @GetMapping("/players/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return "redirect:/admin/players";
    }

    @GetMapping("/posts/news")
    public String listNews(Model model) {
        model.addAttribute("posts", postService.getPostsByCategory("news"));
        model.addAttribute("currentCategory", "news");
        model.addAttribute("content", "admin/posts-list");
        return "layout";
    }

    @GetMapping("/posts/video")
    public String listVideos(Model model) {
        model.addAttribute("posts", postService.getPostsByCategory("video"));
        model.addAttribute("currentCategory", "video");
        model.addAttribute("content", "admin/posts-list");
        return "layout";
    }

    @GetMapping("/posts/history")
    public String listHistory(Model model) {
        model.addAttribute("posts", postService.getPostsByCategory("history"));
        model.addAttribute("currentCategory", "history");
        model.addAttribute("content", "admin/posts-list");
        return "layout";
    }

    @GetMapping("/posts/add")
    public String addPostForm(@RequestParam(defaultValue = "news") String category, Model model) {
        Post post = new Post();
        post.setCategory(category);
        model.addAttribute("post", post);
        model.addAttribute("content", "admin/post-form");
        return "layout";
    }

    @PostMapping("/posts/save")
    public String savePost(@ModelAttribute Post post,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           @RequestParam(value = "videoFile", required = false) MultipartFile videoFile,
                           Model model) {
        try {
            if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Заголовок не может быть пустым");
            }

            String category = post.getCategory();
            boolean hasText = post.getContent() != null && !post.getContent().trim().isEmpty();
            boolean hasNewImage = imageFile != null && !imageFile.isEmpty();
            boolean hasNewVideo = videoFile != null && !videoFile.isEmpty();
            boolean hasExistingImage = post.getImageUrl() != null && !post.getImageUrl().isEmpty();
            boolean hasExistingVideo = post.getVideoUrl() != null && !post.getVideoUrl().isEmpty();

            boolean willHaveImage = hasNewImage || hasExistingImage;
            boolean willHaveVideo = hasNewVideo || hasExistingVideo;

            if (willHaveImage && willHaveVideo) {
                throw new IllegalArgumentException("Пост не может содержать и изображение, и видео одновременно. Выберите что-то одно.");
            }

            if (("news".equals(category) || "video".equals(category)) && !willHaveImage && !willHaveVideo) {
                throw new IllegalArgumentException("Для новостей и видео необходимо загрузить изображение или видео.");
            }

            if (hasNewImage) {
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/posts/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(imageFile.getInputStream(), uploadPath.resolve(fileName));
                post.setImageUrl("/uploads/posts/" + fileName);
                post.setVideoUrl(null); // гарантия
            } else if (!hasExistingImage) {
                post.setImageUrl(null);
            }

            if (hasNewVideo) {
                String fileName = System.currentTimeMillis() + "_" + videoFile.getOriginalFilename();
                Path uploadPath = Paths.get("uploads/videos/");
                if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);
                Files.copy(videoFile.getInputStream(), uploadPath.resolve(fileName));
                post.setVideoUrl("/uploads/videos/" + fileName);
                post.setImageUrl(null); // гарантия
            } else if (!hasExistingVideo) {
                post.setVideoUrl(null);
            }

            if (post.getId() == null) {
                post.setCreatedDate(LocalDateTime.now());
            }
            post.setUpdatedDate(LocalDateTime.now());

            if ("history".equals(category) && post.getDisplayOrder() <= 0) {
                List<Post> history = postService.getPostsByCategory("history");
                int maxOrder = history.stream().mapToInt(Post::getDisplayOrder).max().orElse(0);
                post.setDisplayOrder(maxOrder + 1);
            }

            postService.savePost(post);
            return "redirect:/admin/posts/" + category;
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("post", post);
            model.addAttribute("content", "admin/post-form");
            return "layout";
        } catch (IOException e) {
            model.addAttribute("error", "Ошибка загрузки файла: " + e.getMessage());
            model.addAttribute("post", post);
            model.addAttribute("content", "admin/post-form");
            return "layout";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка сохранения: " + e.getMessage());
            model.addAttribute("post", post);
            model.addAttribute("content", "admin/post-form");
            return "layout";
        }
    }

    @GetMapping("/posts/edit/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.getPostById(id));
        model.addAttribute("content", "admin/post-form");
        return "layout";
    }

    @GetMapping("/posts/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            String category = post.getCategory();
            postService.deletePost(id);
            return "redirect:/admin/posts/" + category;
        }
        return "redirect:/admin/posts/news";
    }

    @GetMapping("/posts/history/moveUp/{id}")
    public String moveUp(@PathVariable Long id) {
        postService.moveUp(id);
        return "redirect:/admin/posts/history";
    }

    @GetMapping("/posts/history/moveDown/{id}")
    public String moveDown(@PathVariable Long id) {
        postService.moveDown(id);
        return "redirect:/admin/posts/history";
    }
}