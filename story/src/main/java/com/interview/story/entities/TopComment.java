package com.interview.story.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class TopComment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String text;
    private String hackApiHandle;
    @JsonIgnore
    private int totalComments;
    @JsonIgnore
    private int commentId;
}
