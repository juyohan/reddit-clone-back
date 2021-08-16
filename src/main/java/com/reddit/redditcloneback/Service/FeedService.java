package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.Error.FeedNotFoundException;
import com.reddit.redditcloneback.Repository.FeedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
                .likeCount(feedDTO.getLikeCount())
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

    // hot은 2틀전부터 현재까지 게시글들 중에서 like의 수가 20이상인 게시글들만 가져옴 (랜덤으로 출력하는건 frontend에서 처리)
    public List<Feed> hotFindFeeds() {
        // 2틀 전 0시 0분 0초부터
        LocalDateTime before = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(0,0,0));
        // 오늘 날 23시 59분 59초까지
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

        return feedRepository.findAllByCreateDateBetweenAndLikeCountIsGreaterThanEqual(before, now, 20);
    }

    // new는 제일 최근에 올라온 피드들 순서대로 정렬해서 가져옴
    public List<Feed> newFindFeeds() {

        return null;
    }
}
