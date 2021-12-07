package com.reddit.redditcloneback.Error;

public class UserNotEmailVerificationException extends RuntimeException{
    public UserNotEmailVerificationException(String message) {
        super(message);
    }
}
