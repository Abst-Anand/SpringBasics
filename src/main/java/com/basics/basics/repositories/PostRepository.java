package com.basics.basics.repositories;

import com.basics.basics.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByPostId(int postId);

    List<Post> findAll();

    Post save(Post post);

}
