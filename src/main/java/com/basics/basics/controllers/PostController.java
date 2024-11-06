package com.basics.basics.controllers;

import com.basics.basics.entities.Post;
import com.basics.basics.repositories.PostRepository;
import com.basics.basics.services.PostService;
import com.basics.basics.services.PostServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    public PostController(PostServiceImpl postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }


    @PostMapping("")
    public Post createPost(@RequestBody Post post){
        return postService.createPost(post);
    }

    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable int postId){
        return postService.getPost(postId);
    }

    @PutMapping("/{postId}")
    public Post updatePost( @RequestBody Post post, @PathVariable int postId){
        return postService.updatePost(postId, post);
    }

}
