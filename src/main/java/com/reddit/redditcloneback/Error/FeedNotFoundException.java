package com.reddit.redditcloneback.Error;

public class FeedNotFoundException extends RuntimeException{
    public FeedNotFoundException(String message) {
        super(message);
    }
}
