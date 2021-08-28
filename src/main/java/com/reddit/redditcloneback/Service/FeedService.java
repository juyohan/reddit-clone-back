package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.LikeType;
import com.reddit.redditcloneback.DAO.Likes;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.DTO.ResponseFeedDTO;
import com.reddit.redditcloneback.Error.FeedNotFoundException;
import com.reddit.redditcloneback.Repository.FeedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class FeedService {

    private FeedRepository feedRepository;
    private UserService userService;
    private LikesService likesService;

//    public List<Feed> findAllFeed() {
//        return feedRepository.findAll();
//    }

    public Page<Feed> findAllFeed(Pageable pageable) {

        return feedRepository.findAll(pageable);
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
    // 페이징 처리 안함 + 댓글 갯수를 이용해 hot으로 올리기
    public List<Feed> hotFindFeeds(Pageable pageable) {
        // 2틀 전 0시 0분 0초부터
        LocalDateTime before = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(0, 0, 0));
        // 오늘 날 23시 59분 59초까지
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        return feedRepository.findAllByCreateDateBetweenAndLikeCountIsGreaterThanEqual(before, now, 20, pageable);
    }

    // new는 제일 최근에 올라온 피드들 순서대로 정렬해서 가져옴
    public List<ResponseFeedDTO> newFindFeeds() {
        // 모든 feed에서 createDate 이름의 컬럼을 기준으로 최신순으로 정렬한 뒤 보냄
        List<Feed> feeds = feedRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));

        return mappingFeedToResponseFeed(feeds);
    }

    // top은 like의 수가 가장 많은 피드들 순서대로 정렬
    public List<ResponseFeedDTO> topFindFeeds(Pageable pageable) {
        // like의 갯수를 내림차 순으로 정렬한 뒤 가져옴
        List<Feed> feeds = feedRepository.findAll(pageable).getContent();

        // Feed 가공
        return mappingFeedToResponseFeed(feeds);
    }

    // rising은 하룻동안 like나, 댓글을 가장 많이 받은 순서대로
    public List<Feed> risingFindFeeds() {
        LocalDateTime before = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        List<Feed> feeds = feedRepository.findAllByCreateDateBetweenOrderByLikeCountDesc(before, now);

        System.out.println("feeds = " + feeds);
        return null;
    }

    // 클라이언트에게 보낼 Data 가공
    private List<ResponseFeedDTO> mappingFeedToResponseFeed(List<Feed> feeds) {
        List<ResponseFeedDTO> list = feeds.stream().map(feed -> {
                    ResponseFeedDTO responseFeedDTO = ResponseFeedDTO.builder()
                            .id(feed.getId())
                            .desc(feed.getDesc())
                            .likeCount(feed.getLikeCount())
                            .title(feed.getTitle())
                            .username(feed.getUser().getUsername())
                            .url(feed.getUrl())
                            .likeType(null)
                            .createDate(feed.getCreateDate())
                            .build();

                    // DB에 접근해서 Likes를 가져온다.
                    Likes likes = likesService.checkLikeTypeToFeed(feed);

                    if(likes != null)
                        responseFeedDTO.setLikeType(likes.getLikeType());

                    return responseFeedDTO;
                })
                .collect(Collectors.toList());
        return list;
    }
}
