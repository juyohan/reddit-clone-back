package com.reddit.redditcloneback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RedditCloneBackApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedditCloneBackApplication.class, args);
    }
}
