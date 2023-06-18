package com.interview.story.services.impl;

import com.interview.story.entities.Comment;
import com.interview.story.entities.Story;
import com.interview.story.entities.TopComment;
import com.interview.story.services.CommentService;
import com.interview.story.utilities.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentServiceImpl implements CommentService {
    private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final Queue<Integer> cacheKeys = new LinkedList<>();


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
                            TopComment topComment = new TopComment(comment.getText(),comment.getBy(), commentCount,  comment.getId());
                            topComments.add(topComment);
                            logger.info("Totals child comment count for the commentId: {} {}", comment.getId(), commentCount);
                        }
                    });
        }

        return topComments.stream()
                .sorted(Comparator.comparingInt(TopComment::getTotalComments).reversed()).limit(10).toList();
    }
}
