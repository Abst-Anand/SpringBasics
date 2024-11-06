package com.basics.basics.services;

import com.basics.basics.entities.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> getAllPosts();

    Post createPost(Post post);

    Post getPost(int id);

    Post updatePost(int id, Post post);
}
