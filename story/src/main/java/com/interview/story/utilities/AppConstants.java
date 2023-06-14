package com.interview.story.utilities;

public class AppConstants {
    private AppConstants(){
        throw new IllegalArgumentException("constructor uses not allowed for this class.");
    }
    public static final String INTERNAL_SERVER_ERROR = "some error occurred on the server.";
    public static final String HACKER_NEWS_API_BASE_URL = "https://hacker-news.firebaseio.com/v0";

}
