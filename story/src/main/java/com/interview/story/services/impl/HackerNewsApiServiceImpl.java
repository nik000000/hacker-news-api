package com.interview.story.services.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import com.interview.story.entities.Comment;
import com.interview.story.entities.Story;
import com.interview.story.entities.TopComment;
import com.interview.story.services.HackerNewsApiService;
import com.interview.story.utilities.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class HackerNewsApiServiceImpl implements HackerNewsApiService {
    private final Logger logger = LoggerFactory.getLogger(HackerNewsApiServiceImpl.class);
    private final Queue<Integer> cacheKeys = new LinkedList<>();
    /**
     * Set to store all past stories served
     */
    private static final Set<Story> pastStories = new HashSet<>();

    /**
     * cache the topTenStories for the next 15 minutes.
     */
    @Override
    @Cacheable("topTenStories")
    public List<Story> getTopTenStories(){
        List<Story> topTenStories = getAllStories().stream().limit(10).toList();
        addToPastStorySet(topTenStories);
        return topTenStories;
    }

    /**
     * Using TreeSet here to sort the elements when we get them using the score of stories.
     * If two stories have the same
     * score, then they will again be compared with score only.
     */
    @Override
    public SortedSet<Story> getAllStories() {
        SortedSet<Story> topStories = new TreeSet<>(Comparator.comparingInt(Story::getScore).reversed().thenComparing(Story::getTitle));
            List<Integer> stories = Utils.getTopStoryIdsFromHackerNews();
            logger.info("Story ids: {}",stories);

            logger.info("getting all the stories");
            stories.parallelStream().forEach(storyId -> {
                Story story = Utils.getStoryById(storyId);
                if (story != null && story.getType().equals("story")) {
                    logger.info("story for id: {}: {}",storyId, story);
                    topStories.add(story);
                }
            });

        return topStories;
    }

    /**
     * Get all the previously served top stories
     *
     * @return Set<Story>
     */
    @Override
    @Cacheable("pastStories")
    public Set<Story> getPastTopStories() {
        return pastStories;
    }


    /**
     * Add top 10 stories to the pastStory set if it does not contain that story, else
     * If it contains that story then check if the score and kids have changed if so then update.
     */
    private void addToPastStorySet(List<Story> topTenStories) {
        logger.info("adding to past stories.");
        topTenStories.parallelStream().forEach(topStory -> {
            if (pastStories.contains(topStory)) {
                pastStories.stream().filter(pastStory -> pastStory.equals(topStory)).findFirst()
                        .ifPresent(story -> story.setScore(topStory.getScore()));
            } else {
                pastStories.add(topStory);
            }
        });
    }

    @CacheEvict(value = "topTenComments",key = "#key")
    public void evictCacheTopTenComments(Integer key){
        logger.info("deleting cache with key: {}",key);
    }
    /**
     * Cache the topTenComments we get for the storyId, here caching each request for storyId as a key. So the results
     * for each storyId will be cached separately for 15minutes and that will improve our applications performance a lot.
     * Sorted set for Top comments in decreasing order
     */
    @Override
    @Cacheable(value = "topTenComments", key = "#storyId")
    public List<TopComment> getTopTenCommentsByStoryId(Integer storyId){
        cacheKeys.add(storyId);
        if(cacheKeys.size() > 10){
            Integer expireCacheKey = cacheKeys.peek();
            cacheKeys.poll();
            evictCacheTopTenComments(expireCacheKey);
        }

        List<TopComment> topComments = new ArrayList<>();

        Story story = Utils.getStoryById(storyId);
        if(story == null)
            return Collections.emptyList();

        int[] childCommentIds = story.getKids();

        if(childCommentIds != null){
            Arrays.stream(childCommentIds)
                    .parallel().forEach(childCommentId->{
                        HashSet<Integer> processedCommentIds = new HashSet<>();
                        Comment comment = Utils.getCommentById(childCommentId);
                        if(comment != null){
                            // the total comment count will be this comment and then all child comments.
                            int commentCount = Utils.getChildCommentCount(comment, processedCommentIds) + 1;
                            TopComment topComment = TopComment.builder()
                                    .totalComments(commentCount)
                                    .hackApiHandle(comment.getBy())
                                    .text(comment.getText())
                                    .commentId(comment.getId())
                                    .build();
                            topComments.add(topComment);
                            logger.info("Totals child comment count for the commentId: {} {}", comment.getId(), commentCount);
                        }
                    });
        }

        return topComments.stream()
                .sorted(Comparator.comparingInt(TopComment::getTotalComments).reversed()).limit(10).toList();
    }
}
