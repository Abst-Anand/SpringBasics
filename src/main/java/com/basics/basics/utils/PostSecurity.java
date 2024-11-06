package com.basics.basics.utils;

import com.basics.basics.entities.Post;
import com.basics.basics.entities.User;
import com.basics.basics.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;

    public boolean isOwner(int postId){

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Post post = postService.getPost(postId);

        return currentUser.getUserId().equals(post.getAuthor().getUserId());

    }
}
