package com.interview.story.exceptions;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiErrorResponse {
    private Date timestamp;
    private String message;
}
