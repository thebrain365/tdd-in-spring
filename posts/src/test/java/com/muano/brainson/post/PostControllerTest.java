package com.muano.brainson.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;

    List<Post> posts = new ArrayList<>();

    @BeforeEach
    void setUp() {
        posts = List.of(
                new Post(1, 1, "Test post 1", "Hello, World", null),
                new Post(2, 1, "Test post 2", "My name is Muano", null)
        );
    }

    @Test
    public void shouldReturnAllPosts() throws Exception {

        String jsonResponse = """
                [
                    {
                        "id":1,
                        "userId":1,
                        "title":"Test post 1",
                        "body":"Hello, World",
                        "version": null
                    },
                    {
                        "id":2,
                        "userId":1,
                        "title":"Test post 2",
                        "body":"My name is Muano",
                        "version": null
                    }
                ]
                """;

        when(postRepository.findAll()).thenReturn(posts);

        mockMvc.perform(get("/api/posts"))
                .andExpectAll(
                        status().isOk(),
                        content().json(jsonResponse)
                );
    }

    @Test
    void shouldFindPostWhenGivenValidId() throws Exception {
        when(postRepository.findById(1)).thenReturn(Optional.of(posts.get(0)));

        String json = """
                    {
                        "id":1,
                        "userId":1,
                        "title":"Test post 1",
                        "body":"Hello, World",
                        "version": null
                    }
                """;

        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk());
    }
}













