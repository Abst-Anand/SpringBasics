package com.basics.basics.services;

import com.basics.basics.entities.Post;
import com.basics.basics.entities.User;
import com.basics.basics.exceptions.ResourceNotFoundException;
import com.basics.basics.exceptions.TestException;
import com.basics.basics.repositories.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post createPost(Post post) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        post.setAuthor(user);

        return postRepository.save(post);
    }

    @Override
    public Post getPost(int id) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("User is : {}", user);

        return postRepository.findByPostId(id).orElseThrow(()->new ResourceNotFoundException("Post not found with post id " + id));
    }

    @Override
    public Post updatePost(int postId, Post updatedPost){

        Post oldPost = getPost(postId);

        oldPost.setTitle(updatedPost.getTitle());

        return createPost(oldPost);

    }
}
