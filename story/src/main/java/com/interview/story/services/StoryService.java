package com.interview.story.services;

import com.interview.story.entities.Story;

import java.util.List;
import java.util.Set;

public interface StoryService {
    /**
     * will return the top 10 stories.
     * @return
     */
    List<Story> getTopTenStories();
    Set<Story> getPastTopStories();
}
