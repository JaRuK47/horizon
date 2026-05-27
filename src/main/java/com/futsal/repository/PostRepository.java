package com.futsal.repository;

import com.futsal.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByCategoryOrderByDisplayOrderAsc(String category);

    List<Post> findByCategoryOrderByIdDesc(String category);

    List<Post> findByCategoryOrderByCreatedDateDesc(String category);
}