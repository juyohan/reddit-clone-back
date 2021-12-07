package com.reddit.redditcloneback;

import com.reddit.redditcloneback.Repository.FeedRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedditCloneBackApplicationTests {

    private final FeedRepository feedRepository;

    RedditCloneBackApplicationTests(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

//    @Test
//    public void pagingTest() {
//        feedRepository.findAll()
//    }

}
