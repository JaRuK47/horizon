package com.futsal.service;

import com.futsal.model.Post;
import com.futsal.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private Post oldPost, newPost;

    @BeforeEach
    void setUp() {
        oldPost = new Post();
        oldPost.setId(1L);
        oldPost.setTitle("Старая новость");
        oldPost.setCategory("news");
        oldPost.setCreatedDate(LocalDateTime.of(2024, 1, 1, 10, 0));
        oldPost.setDisplayOrder(1);

        newPost = new Post();
        newPost.setId(2L);
        newPost.setTitle("Новая новость");
        newPost.setCategory("news");
        newPost.setCreatedDate(LocalDateTime.of(2025, 1, 1, 10, 0));
        newPost.setDisplayOrder(2);
    }

    @Test
    void getNewsPosts_ShouldReturnNewestFirst() {
        when(postRepository.findByCategoryOrderByIdDesc("news"))
                .thenReturn(List.of(newPost, oldPost));

        List<Post> news = postService.getNewsPosts();

        assertEquals(2, news.size());
        assertEquals("Новая новость", news.get(0).getTitle());
        assertEquals("Старая новость", news.get(1).getTitle());
        verify(postRepository, times(1)).findByCategoryOrderByIdDesc("news");
    }

    @Test
    void getHistoryPosts_ShouldReturnOrderedByDisplayOrder() {
        when(postRepository.findByCategoryOrderByDisplayOrderAsc("history"))
                .thenReturn(List.of(oldPost, newPost));

        List<Post> history = postService.getHistoryPosts();

        assertEquals(2, history.size());
        assertEquals(1, history.get(0).getDisplayOrder());
        assertEquals(2, history.get(1).getDisplayOrder());
        verify(postRepository, times(1)).findByCategoryOrderByDisplayOrderAsc("history");
    }
}