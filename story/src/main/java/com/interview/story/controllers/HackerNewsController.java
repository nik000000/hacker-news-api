package com.interview.story.controllers;

import com.interview.story.entities.Story;
import com.interview.story.entities.TopComment;
import com.interview.story.services.CommentService;
import com.interview.story.services.StoryService;
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
    private final CommentService commentService;
    private final StoryService storyService;

    @Autowired
    public HackerNewsController(CommentService commentService, StoryService storyService) {
        this.commentService = commentService;
        this.storyService = storyService;
    }


    @GetMapping("/top-stories")
    public ResponseEntity<List<Story>> getTopStories(){
        List<Story> topStories = storyService.getTopTenStories();
        return ResponseEntity.ok(topStories);
    }

    @GetMapping("/past-stories")
    public ResponseEntity<Set<Story>> getAllPastStories(){
        Set<Story> pastStories = storyService.getPastTopStories();
        return ResponseEntity.ok(pastStories);
    }

    @GetMapping("/comments/{storyId}")
    public ResponseEntity<List<TopComment>> getTopTenCommentsSortedByCountOfChildComments(@PathVariable Integer storyId){
        return ResponseEntity.ok(commentService.getTopTenCommentsByStoryId(storyId));
    }
}
