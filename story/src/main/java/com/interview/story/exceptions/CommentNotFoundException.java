package com.interview.story.exceptions;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException(String msg){
        super(msg);
    }
}
