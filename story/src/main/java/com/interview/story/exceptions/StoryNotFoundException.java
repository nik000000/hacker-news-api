package com.interview.story.exceptions;

public class StoryNotFoundException extends   RuntimeException{
    public StoryNotFoundException(String msg){
        super(msg);
    }
}
