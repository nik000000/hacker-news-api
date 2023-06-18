package com.interview.story.services.impl;

import com.interview.story.entities.Story;
import com.interview.story.scheduled.ScheduledTasks;
import com.interview.story.services.StoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoryServiceImpl implements StoryService {
    private final Logger logger = LoggerFactory.getLogger(StoryServiceImpl.class);
    private final ScheduledTasks scheduledTasks;
    /**
     * Set to store all past stories served
     */
    private static Set<Story> pastStories = new LinkedHashSet<>();

    @Autowired
    public StoryServiceImpl(ScheduledTasks scheduledTasks) {
        this.scheduledTasks = scheduledTasks;
    }

    @Override
    public List<Story> getTopTenStories() {
        List<Story> topTenStories = scheduledTasks.getAllStoriesFromHackerNewsApi().stream().limit(10).toList();
        this.addToPastStorySet(topTenStories);
        return topTenStories;
    }

    /**
     * Get all the previously served top stories
     *
     * @return Set<Story>
     */
    @Override
    public Set<Story> getPastTopStories() {
        return pastStories;
    }

    /**
     * Add top 10 stories to the pastStory set if it does not contain that story, else
     * If it contains that story then check if the score and kids have changed if so then update.
     */
    private void addToPastStorySet(List<Story> topTenStories) {
        logger.info("adding to past stories.");
        topTenStories.stream().forEach(topStory -> {
            if (pastStories.contains(topStory)) {
                pastStories.stream().filter(pastStory -> pastStory.equals(topStory)).findFirst()
                        .ifPresent(story -> story.setScore(topStory.getScore()));
            } else {
                pastStories.add(topStory);
            }
        });
    }
}
