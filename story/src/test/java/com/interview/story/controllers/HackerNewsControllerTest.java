package com.interview.story.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.story.entities.Story;
import com.interview.story.entities.TopComment;
import com.interview.story.services.impl.HackerNewsApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
class HackerNewsControllerTest {

    @MockBean
    private HackerNewsApiServiceImpl hackerNewsApiService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static LinkedList<Story> allStories;
    private static LinkedList<TopComment> topTenComments;

    @BeforeEach
    public void beforeEachTest() throws IOException {
        // load stories data into the list.
        Resource resource = resourceLoader.getResource("classpath:data.txt");
        InputStream inputStream = resource.getInputStream();

        // Read JSON data from the file and map it to a List of Story objects
        allStories = objectMapper.readValue(inputStream, new TypeReference<>() {});

        // load stories data into the list.
        resource = resourceLoader.getResource("classpath:childComments.txt");
        inputStream = resource.getInputStream();

        // Read JSON data from the file and map it to a List of Story objects
        topTenComments = objectMapper.readValue(inputStream, new TypeReference<>() {});
        topTenComments.stream().forEach(System.out::println);
    }


    @Test
    void getTopStories() throws Exception {
        Mockito.when(hackerNewsApiService.getTopTenStories()).thenReturn(allStories);

        // actual request for url.
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/stories/top-stories").characterEncoding("UTF-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].by").value("Freddie111"))
                .andExpect(jsonPath("$[0].score").value(1957))
                .andExpect(jsonPath("$[0].title").value("Reddit Strike Has Started"));
    }



    @Test
    void getAllPastStories() throws Exception {
        Mockito.when(hackerNewsApiService.getPastTopStories()).thenReturn(allStories.stream().collect(Collectors.toSet()));


        // actual request for url.
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/stories/past-stories").characterEncoding("UTF-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].by").exists());
    }


    @Test
    void getTopTenCommentsSortedByCountOfChildComments() throws Exception {
        Mockito.when(hackerNewsApiService.getTopTenCommentsByStoryId(Mockito.anyInt())).thenReturn(topTenComments);
        // actual request for url.
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/stories/comments/36270597").characterEncoding("UTF-8")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").exists())
                .andExpect(jsonPath("$[0].hackApiHandle").value("Tinyyy"));
    }
}