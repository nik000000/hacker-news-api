package com.interview.story.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Story implements Serializable {
    private static final long serialVersionUID = 1L;
    private String by;
    private int score;

    private int time;
    private String title;
    private String url;
//    @JsonSerialize(using = MillisToLocalDateTimeSerializer.class)
    @JsonProperty
    public int getTime() {
        return time;
    }

    @JsonProperty
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * Fields to be used but not returned in response
     */
    @Getter(onMethod = @__(@JsonIgnore))
    @Setter(onMethod = @__(@JsonProperty))
    private int[] kids;
    private String type;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Story other = (Story) obj;
        // Implement the equality check based on the desired fields
        return Objects.equals(by, other.by) &&
                score == other.score &&
                time == other.time &&
                Objects.equals(title, other.title) &&
                Objects.equals(url, other.url) &&
                Arrays.equals(kids, other.kids) &&
                Objects.equals(type, other.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(by, score, time, title, url, Arrays.hashCode(kids), type);
    }

}
