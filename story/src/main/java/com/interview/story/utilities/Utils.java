package com.interview.story.utilities;

import com.interview.story.entities.Comment;
import com.interview.story.entities.Story;
import com.interview.story.exceptions.CommentNotFoundException;
import com.interview.story.exceptions.InternalServerException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class Utils {
    private Utils(){
        throw new UnsupportedOperationException("constructor usage not allowed for utility class.");
    }

    private static final WebClient webClient = WebClient.create(AppConstants.HACKER_NEWS_API_BASE_URL);
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static List<Integer> getTopStoryIdsFromHackerNews(){
        List<Integer> topStories;
        try{
            ParameterizedTypeReference<List<Integer>> typeReference= new ParameterizedTypeReference<>(){};
            topStories = webClient.get().uri("/topstories.json").retrieve().bodyToMono(typeReference).block();
        } catch (Exception e) {
            logger.error(e.toString());
            throw new InternalServerException(AppConstants.INTERNAL_SERVER_ERROR);
        }

        return topStories;
    }



    public static Story getStoryById(Integer storyId){
        Story story;
        try{
            story = webClient.get().uri("/item/"+storyId+".json").retrieve().bodyToMono(Story.class).block();
        }
        catch (Exception e) {
            logger.error(e.toString());
            throw new InternalServerException(AppConstants.INTERNAL_SERVER_ERROR);
        }

        return story;
    }

    public static Comment getCommentById(Integer commentId){
        Comment comment;
        try{
            comment = webClient.get().uri("/item/"+commentId+".json").retrieve().bodyToMono(Comment.class).block();
        }
        catch (Exception e) {
            logger.error(e.toString());
            throw new InternalServerException(AppConstants.INTERNAL_SERVER_ERROR);
        }
        if(comment == null){
            throw new CommentNotFoundException("comment not found with id: "+commentId);
        }
        return comment;
    }

    public static int getChildCommentCount(Comment comment, Set<Integer> processedCommentIds) {

        if (comment == null) {
            return 0;
        }

        AtomicInteger commentCount = new AtomicInteger(1);
        if (!processedCommentIds.contains(comment.getId())) {
            processedCommentIds.add(comment.getId());
            if (comment.getKids() != null) {
                commentCount.addAndGet(comment.getKids().length);

                Arrays.stream(comment.getKids()).parallel().forEach(commentId -> {
                    Comment childComment = getCommentById(commentId);
                    commentCount.addAndGet(getChildCommentCount(childComment, processedCommentIds));
                });
            }
        }

        return commentCount.get();
    }
}
