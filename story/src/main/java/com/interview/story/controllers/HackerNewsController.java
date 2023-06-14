package com.interview.story.controllers;

import com.interview.story.entities.Story;
import com.interview.story.entities.TopComment;
import com.interview.story.services.impl.HackerNewsApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/stories")
public class HackerNewsController {
    private final HackerNewsApiServiceImpl hackerNewsApiServiceImpl;

    @Autowired
    public HackerNewsController(HackerNewsApiServiceImpl hackerNewsApiServiceImpl) {
        this.hackerNewsApiServiceImpl = hackerNewsApiServiceImpl;
    }

    @GetMapping("/top-stories")
    public ResponseEntity<List<Story>> getTopStories(){
        List<Story> topStories = hackerNewsApiServiceImpl.getTopTenStories();
        return ResponseEntity.ok(topStories);
    }

    @GetMapping("/past-stories")
    public ResponseEntity<Set<Story>> getAllPastStories(){
        Set<Story> pastStories = hackerNewsApiServiceImpl.getPastTopStories();
        return ResponseEntity.ok(pastStories);
    }

    @GetMapping("/comments/{storyId}")
    public ResponseEntity<List<TopComment>> getTopTenCommentsSortedByCountOfChildComments(@PathVariable Integer storyId){
        return ResponseEntity.ok(hackerNewsApiServiceImpl.getTopTenCommentsByStoryId(storyId));
    }
}
