package com.interview.story.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    private String id;
    private LocalDateTime createdAt;
    private Integer karma;
    private String about;
    private List<Integer> activities;
}
