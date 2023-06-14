package com.interview.story.exceptions;

public class InternalServerException extends RuntimeException{
    private static final long serialVersionUID = 1L;

	public InternalServerException(String msg){
        super(msg);
    }
}
