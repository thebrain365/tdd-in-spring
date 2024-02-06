package com.muano.brainson.post;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;

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

    @Test
    void shouldNotFindPostWhenGivenInvalidId() throws Exception {
        when(postRepository.findById(999)).thenThrow(PostNotFoundException.class);

        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateNewPostWhenPostIsValid() throws Exception {
        var newPost = new Post(3, 1, "New title", "New body", null);
        var json = objectMapper.writeValueAsString(newPost);

        when(postRepository.save(newPost)).thenReturn(newPost);

        mockMvc.perform(post("/api/posts")
                    .contentType("application/json")
                    .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateNewPostWhenPostIsInvalid() throws Exception {
        var newPost = new Post(3, 1, "", "", null);
        var jsonContent = objectMapper.writeValueAsString(newPost);

        when(postRepository.save(newPost)).thenReturn(newPost);

        mockMvc.perform(post("/api/posts")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdatePostWhenGivenValidPost() throws Exception {
        var updatedPost = new Post(1, 1, "Updated title", "Updated body", null);
        var jsonContent = objectMapper.writeValueAsString(updatedPost);

        when(postRepository.findById(1)).thenReturn(Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);

        mockMvc.perform(put("/api/posts/1")
                    .contentType("application/json")
                    .content(jsonContent))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeletePostWhenGivenValidId() throws Exception {
        doNothing().when(postRepository).deleteById(1);

        mockMvc.perform(delete("/api/posts/1"))
                .andExpect(status().isNoContent());

        verify(postRepository, times(1)).deleteById(1);
    }
}













