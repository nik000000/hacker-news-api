package com.interview.story.services;

import com.interview.story.entities.TopComment;

import java.util.List;

public interface CommentService {
    List<TopComment> getTopTenCommentsByStoryId(Integer storyId);
}
