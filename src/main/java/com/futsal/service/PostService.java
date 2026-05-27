package com.futsal.service;

import com.futsal.model.Post;
import com.futsal.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getHistoryPosts() {
        return postRepository.findByCategoryOrderByDisplayOrderAsc("history");
    }

    public List<Post> getNewsPosts() {
        return postRepository.findByCategoryOrderByIdDesc("news");
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByCategory(String category) {
        return postRepository.findByCategoryOrderByCreatedDateDesc(category);
    }

    public void moveUp(Long id) {
        Post current = postRepository.findById(id).orElseThrow();
        List<Post> allHistory = postRepository.findByCategoryOrderByDisplayOrderAsc("history");
        int currentIndex = -1;
        for (int i = 0; i < allHistory.size(); i++) {
            if (allHistory.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex > 0) {
            Post above = allHistory.get(currentIndex - 1);
            int tempOrder = current.getDisplayOrder();
            current.setDisplayOrder(above.getDisplayOrder());
            above.setDisplayOrder(tempOrder);
            postRepository.save(current);
            postRepository.save(above);
        }
    }

    public void moveDown(Long id) {
        Post current = postRepository.findById(id).orElseThrow();
        List<Post> allHistory = postRepository.findByCategoryOrderByDisplayOrderAsc("history");
        int currentIndex = -1;
        for (int i = 0; i < allHistory.size(); i++) {
            if (allHistory.get(i).getId().equals(id)) {
                currentIndex = i;
                break;
            }
        }
        if (currentIndex < allHistory.size() - 1) {
            Post below = allHistory.get(currentIndex + 1);
            int tempOrder = current.getDisplayOrder();
            current.setDisplayOrder(below.getDisplayOrder());
            below.setDisplayOrder(tempOrder);
            postRepository.save(current);
            postRepository.save(below);
        }
    }

    public List<Post> getVideos() {
        return postRepository.findByCategoryOrderByCreatedDateDesc("video");
    }
}