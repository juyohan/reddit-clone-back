package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.LikeType;
import com.reddit.redditcloneback.DAO.Likes;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.LikeDTO;
import com.reddit.redditcloneback.Error.FeedNotFoundException;
import com.reddit.redditcloneback.Repository.FeedRepository;
import com.reddit.redditcloneback.Repository.LikesRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.reddit.redditcloneback.DAO.LikeType.UPLIKE;

@Service
@AllArgsConstructor
@Slf4j
public class LikesService {

    private final LikesRepository likesRepository;
    private final FeedRepository feedRepository;
    private final UserService userService;

    @Transactional
    public Integer like(LikeDTO likeDTO) {
        Feed feed = feedRepository.findById(likeDTO.getFeedId())
                .orElseThrow(() -> new FeedNotFoundException("존재하지 않는 " + likeDTO.getFeedId() + " 입니다."));
        Optional<Likes> likesByUserAndPost = likesRepository.findByFeedAndUserOrderByIdDesc(feed, userService.getCurrentUser());
        // .isPresent() : 해당 값이 null인지 아닌지 여부를 알려준다.
        // 이미 좋아요나 싫어요를 눌렀다면
        if (likesByUserAndPost.isPresent()) {
            // 누른 버튼이 이미 누른 버튼과 같다면
            if (likesByUserAndPost.get().getLikeType().equals(likeDTO.getLikeType())) {
                deleteLike(likesByUserAndPost);
                return checkLikeTypeSaveLikeCount(feed, likeDTO, -1);

            } else { // 버튼이 다르다면
                deleteLike(likesByUserAndPost);
                // 새로운 좋아요 값 저장
                likesRepository.save(mapToLike(likeDTO, feed));
                return checkLikeTypeSaveLikeCount(feed, likeDTO, 2);
            }
        } else { // 만약, 좋아요의 데이터가 없다면
            likesRepository.save(mapToLike(likeDTO, feed));
            return checkLikeTypeSaveLikeCount(feed, likeDTO, 1);
        }
    }

    // Like에 값 저장
    private Likes mapToLike(LikeDTO likeDTO, Feed feed) {
        return Likes.builder()
                .likeType(likeDTO.getLikeType())
                .feed(feed)
                .user(userService.getCurrentUser())
                .build();
    }

    // 변경되는 좋아요 값 변경
    private Integer checkLikeTypeSaveLikeCount(Feed feed, LikeDTO likeDTO, int count) {
        if (UPLIKE.equals(likeDTO.getLikeType()))
            feed.setLikeCount(feed.getLikeCount() + count);
        else
            feed.setLikeCount(feed.getLikeCount() - count);

        return feedRepository.save(feed).getLikeCount();
    }

    // 연관관계 해제 후 삭제
    private void deleteLike(Optional<Likes> likes) {
        likes.get().setFeed(null);
        likes.get().setUser(null);

        likesRepository.deleteById(likes.get().getId());
    }

    // Like를 눌렀는지 안눌렀는지 확인하는 함수
    public LikeType checkLikeToFeed(LikeDTO likeDTO) {
        Feed feed = feedRepository.findById(likeDTO.getFeedId()).orElseThrow(
                () -> new FeedNotFoundException(likeDTO.getFeedId() + "의 게시글을 찾지 못했습니다.")
        );
//        Feed feed = feedRepository.findById(feedId).orElseThrow(
//                () -> new FeedNotFoundException(feedId + "를 찾을 수 없습니다.")
//        );
        Optional<Likes> likesByUserAndFeed = likesRepository.findByFeedAndUserOrderByIdDesc(feed, userService.getCurrentUser());
//        Optional<Likes> likesByUser = likesRepository.findByUser(userService.getCurrentUser());
//        List<Likes> likesByUser = likesRepository.findByUser(userService.getCurrentUser());
//        List<LikeType> likeType = likesByUser.stream().map(likes -> likes.getLikeType()).collect(Collectors.toList());

        if (likesByUserAndFeed.isPresent()) {
            return likesByUserAndFeed.get().getLikeType();
        } else {
            return null;
        }
//        return likesByUser.get().getLikeType();
    }

    public LikeType checkLikeTypeToFeed(Feed feed) {
        Optional<Likes> likesByUserAndFeed = likesRepository.findByFeedAndUserOrderByIdDesc(feed, userService.getCurrentUser());

//        System.out.println("likesByUserAndFeed = " + likesByUserAndFeed.isPresent());
//        System.out.println("likesByUserAndFeed.isEmpty() = " + likesByUserAndFeed.isEmpty());
//
        if (!likesByUserAndFeed.isPresent())
            return null;
        else
            return likesByUserAndFeed.get().getLikeType();
//        Optional<Likes> likesByUser = likesRepository.findByUser(userService.getCurrentUser());

//        return feeds.stream().map(feed -> {
//            if(likesByUser.get().getId() == feed.getId()) {
//                return likesByUser.get().getLikeType();
//            }
//            else
//                return null;
//        }).collect(Collectors.toList());
    }
}
