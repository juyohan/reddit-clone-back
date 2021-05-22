package com.reddit.redditcloneback.Error;

public class SpringRedditException extends RuntimeException{
    public SpringRedditException(String message) {
        super(message);
    }
}
