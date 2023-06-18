package com.interview.story.scheduled;

import com.interview.story.entities.Story;
import com.interview.story.utilities.Utils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Getter
public class ScheduledTasks {

    private SortedSet<Story> allStoriesFromHackerNewsApi = new TreeSet<>(Comparator.comparingInt(Story::getScore).reversed().thenComparing(Story::getTitle));
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    /**
     * Using TreeSet here to sort the elements when we get them using the score of stories.
     * If two stories have the same
     * score, then they will again be compared with score only.
     */
    @Scheduled(fixedDelay = 900000)
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 5000)) // Retry up to 3 times with a backoff delay of 5 seconds
    public SortedSet<Story> getAllStories() {
        SortedSet<Story> topStories = Collections.synchronizedSortedSet(new TreeSet<>(Comparator.comparingInt(Story::getScore).reversed().thenComparing(Story::getTitle)));
        List<Integer> stories = Utils.getTopStoryIdsFromHackerNews();
        logger.info("Story ids: {}",stories);

        logger.info("getting all the stories");

        stories.parallelStream()
                .map(Utils::getStoryById)
                .filter(story-> story!=null && story.getType().equals("story"))
                .forEach(topStories::add);

        allStoriesFromHackerNewsApi = topStories;
        return topStories;
    }
}
