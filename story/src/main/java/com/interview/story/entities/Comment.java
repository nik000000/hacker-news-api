package com.interview.story.entities;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private String by;
    private int id;
    private int parent;
    private int[] kids;
    private String text;
    private int time;
    private String type;
}
