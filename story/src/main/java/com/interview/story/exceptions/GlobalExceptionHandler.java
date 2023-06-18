package com.interview.story.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ApiErrorResponse> handleInternalServerErrors(InternalServerException exception){
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(StoryNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleStoryNotFoundException(StoryNotFoundException exception){
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleCommentNotFoundException(CommentNotFoundException exception){
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .message(exception.getMessage())
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
