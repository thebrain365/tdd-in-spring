package com.muano.brainson.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
class PostController {

    @GetMapping("")
    List<Post> findAll() {
        return null;
    }
}
