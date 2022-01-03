package com.reddit.redditcloneback;

import com.reddit.redditcloneback.dto.LikeDto;
import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.model.enumeration.LikeType;
import com.reddit.redditcloneback.repository.FeedRepository;
import com.reddit.redditcloneback.repository.LikeRepository;
import com.reddit.redditcloneback.repository.UserRepository;
import com.reddit.redditcloneback.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LikeTest {

    @Autowired
    LikeService likeService;
    @Autowired
    LikeRepository likeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FeedRepository feedRepository;

    @BeforeEach
    public void initialData() {
        User user = User.builder()
                .enable(true)
                .imagePath("")
                .password("asdf")
                .email("dygks8557@naver.com")
                .nickname("jupaka")
                .build();

        userRepository.save(user);

//        String key = new TempKey().getKey(6, false);
        Feed feed = Feed.builder()
                .title("FAKE TITLE 1")
                .contents("FAKE CONTENTS 1")
                .uid("abcdef")
                .user(user)
                .build();

        feedRepository.save(feed);

        Feed feed1 = Feed.builder()
                .title("FAKE TITLE 2")
                .contents("FAKE CONTENTS 2")
                .uid("abcdef1")
                .user(user)
                .build();

        feedRepository.save(feed1);

//        Like like = Like.builder()
//                .type(LikeType.DOWN_LIKE)
//                .user(user)
//                .feed(feed)
//                .build();
//
//        likeRepository.save(like);
    }

    @Test
    public void test() {
        LikeDto.Request.ClickLike clickLike = new LikeDto.Request.ClickLike();
        clickLike.setUid("abcdef");
        clickLike.setType(LikeType.DOWN_LIKE);

        likeService.checkLikedOfFeed(clickLike);
    }
}
