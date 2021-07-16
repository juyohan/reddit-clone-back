package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.AuthKey.TempKey;
import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.Repository.FeedRepository;
import com.reddit.redditcloneback.Util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class FeedService {

    private FeedRepository feedRepository;
    private UserService userService;

    public List<Feed> getAll() {
        return feedRepository.findAll();
    }

    public Feed saveFeed(FeedDTO feedDTO) {
        User user = userService.loginAfterFindUserName();

//        String feedPath = feedUrl(user.getUsername());

        Feed feed = Feed.builder()
                .title(feedDTO.getTitle())
                .desc(feedDTO.getDesc())
                .user(user)
//                .url(feedPath)
                .build();

//        feed.addUser(user);
        feed.setCreateDate(feedDTO.getCreateDate());

        return feedRepository.save(feed);
    }

//    private String feedUrl(String username) {
//
//    }

    public List<Feed> allFeedsFindUser(String username) {
        User user = userService.loginAfterFindUserName(username);

        List<Feed> feeds = feedRepository.findFeedsByUser(user);
        return feeds;
    }
}
