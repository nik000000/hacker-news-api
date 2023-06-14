package com.interview.story.services;

import com.interview.story.entities.Story;
import com.interview.story.entities.TopComment;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public interface HackerNewsApiService {
    /**
     * will return the top 10 stories.
     * @return
     */
    List<Story> getTopTenStories();
    SortedSet<Story> getAllStories();
    Set<Story> getPastTopStories();
    List<TopComment> getTopTenCommentsByStoryId(Integer storyId);
}
