package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.Error.FeedNotFoundException;
import com.reddit.redditcloneback.Repository.FeedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FeedService {

    private FeedRepository feedRepository;
    private UserService userService;

    public List<Feed> findAllFeed() {
        return feedRepository.findAll();
    }

    // 피드 저장
    public Feed saveFeed(FeedDTO feedDTO) {
        User user = userService.getCurrentUser();

//        String feedPath = feedUrl(user.getUsername());

        Feed feed = Feed.builder()
                .title(feedDTO.getTitle())
                .desc(feedDTO.getDesc())
                .user(user)
//                .url(feedPath)
                .build();

//        feed.addUser(user);
        feed.setCreateDate(LocalDateTime.now());

        return feedRepository.save(feed);
    }

    // 피드 수정
    public Feed modifiedFeed(FeedDTO feedDTO) {
        User user = userService.getCurrentUser();

        Feed feed = feedRepository.findByUserAndId(user, feedDTO.getFeedId()).orElseThrow(() -> new FeedNotFoundException("없는 유저나 피드입니다."));

        return feed;
    }


//    public List<Feed> allFeedsFindUser(String username) {
//        User user = userService.getCurrentUser();
//
//        List<Feed> feeds = feedRepository.findFeedsByUser(user);
//        return feeds;
//    }

    // 일정 정해진 Like의 수가 넘어가면 hot으로 넘김
    // Like의 수는 400개?
    public List<Feed> hotFindFeeds() {

        return null;
    }

}
