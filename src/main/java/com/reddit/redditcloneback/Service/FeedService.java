package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.*;
import com.reddit.redditcloneback.Error.FeedNotFoundException;
import com.reddit.redditcloneback.Key.TempKey;
import com.reddit.redditcloneback.Repository.FeedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 피드의 정보에 접근할 때, 유저의 정보를 항상 같이 가져가야하는가?
 * 피드의 정보에 접근할 때, fetch join 으로 맨 처음에 다같이 가져와야할까 아니면, 지금처럼 따로 접근을 시키는게 나을까?
 **/
@Service
@AllArgsConstructor
@Slf4j
public class FeedService {

    private FeedRepository feedRepository;
    private UserService userService;
    private LikesService likesService;

    // 피드 저장
    public Feed saveFeed(RequestFeedDTO requestFeedDTO) {
        // 로그인 되어있는 사용자 정보 가져옴
        User user = userService.loginAfterFindUserName();
        // 지정 URL 설정
        String key = new TempKey().getKey(6, false);

        Feed feed = Feed.builder()
                .title(requestFeedDTO.getTitle())
                .content(requestFeedDTO.getContent())
                .user(user)
                // 처음 좋아요는 0으로 Default
                .likeCount(0)
                .feedKey(key)
                .build();

        // 생성된 시간 저장
        feed.setCreateDate(LocalDateTime.now());

        return feedRepository.save(feed);
    }

    // 피드 수정
    public Feed modifiedFeed(FeedDTO feedDTO) {
        User user = userService.loginAfterFindUserName();

        Feed feed = feedRepository.findByUserAndId(user, feedDTO.getFeedId()).orElseThrow(() -> new FeedNotFoundException("없는 유저나 피드입니다."));

        return feed;
    }

    // hot은 2틀전부터 현재까지 게시글들 중에서 like의 수가 20이상인 게시글들만 가져옴 (랜덤으로 출력하는건 frontend에서 처리)
    // 댓글 갯수를 이용해 hot으로 올리기
    public Result hotFindFeeds(Pageable pageable) {
        // 2틀 전 0시 0분 0초부터
        LocalDateTime before = LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(0, 0, 0));
        // 오늘 날 23시 59분 59초까지
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        Page<Feed> feeds = feedRepository.findAllByCreateDateBetweenAndLikeCountIsGreaterThanEqual(before, now, 20, pageable);

        return mergeFeed(feeds.getContent(), feeds.getTotalPages());
    }

    // new는 제일 최근에 올라온 피드들 순서대로 정렬해서 가져옴
    public Result newFindFeeds(Pageable pageable) {
        // 모든 feed에서 createDate 이름의 컬럼을 기준으로 최신순으로 정렬한 뒤 보냄
        Page<Feed> feedPage = feedRepository.findAll(pageable);

        return mergeFeed(feedPage.getContent(), feedPage.getTotalPages());
//        return feedRepository.findAll(pageable);
    }

    // top은 like의 수가 가장 많은 피드들 순서대로 정렬
    public Result topFindFeeds(Pageable pageable) {
        // like의 갯수를 내림차 순으로 정렬한 뒤 가져옴 (25개씩)
        Page<Feed> feedPage = feedRepository.findAll(pageable);

        return mergeFeed(feedPage.getContent(), feedPage.getTotalPages());
    }

    // rising은 하룻동안 like나, 댓글을 가장 많이 받은 순서대로
    public Result risingFindFeeds(Pageable pageable) {
        LocalDateTime before = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0, 0, 0));
        LocalDateTime now = LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59));

        Page<Feed> feeds = feedRepository.findAllByCreateDateBetweenOrderByLikeCountDesc(before, now, pageable);

        return mergeFeed(feeds.getContent(), feeds.getTotalPages());
    }

    // 좋아요 검색 후 DTO로 변환한 뒤, 다시 랩핑
    private Result mergeFeed(List<Feed> feeds, int total) {
        // 가져온 피드들에서 로그인한 회원이 좋아요를 누른 피드가 있는지 확인 한 후 반환
        List<LikeDTO> likes = likesService.likes(feeds);
        // 클라이언트에게 보낼 데이터 가공
        List<ResponseFeedDTO> responseFeedDTOS = mappingFeedAndLikesToResponseFeed(feeds, likes);

        return new Result(responseFeedDTOS, total);
    }

//  클라이언트에게 보낼 Data 가공
    private List<ResponseFeedDTO> mappingFeedAndLikesToResponseFeed(List<Feed> feeds, List<LikeDTO> likes) {
        // 가져온 게시글들을 DTO로 변환
        List<ResponseFeedDTO> responseFeedDTOS = feeds.stream().map(ResponseFeedDTO::new).collect(Collectors.toList());

        // 좋아요의 값이 없는 경우
        if (likes == null)
            return responseFeedDTOS;

        // 좋아요를 누른 게시글에 좋아요 값을 넣기 위한 루프
        Consumer<ResponseFeedDTO> dtoConsumer = responseFeedDTO -> {
            for (LikeDTO likeDTO : likes) {
                if (likeDTO.getFeedId() == responseFeedDTO.getFeedId()) {
                    responseFeedDTO.setLikeType(likeDTO.getLikeType());
                    break;
                }
            }
        };

        responseFeedDTOS.stream().forEach(dtoConsumer);
        return responseFeedDTOS;
    }
}
