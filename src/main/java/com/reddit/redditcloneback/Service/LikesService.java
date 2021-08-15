package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.LikeType;
import com.reddit.redditcloneback.DAO.Likes;
import com.reddit.redditcloneback.DTO.LikeDTO;
import com.reddit.redditcloneback.Error.FeedNotFoundException;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.Repository.FeedRepository;
import com.reddit.redditcloneback.Repository.LikesRepository;
import com.reddit.redditcloneback.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.reddit.redditcloneback.DAO.LikeType.UPLIKE;

@Service
@AllArgsConstructor
@Slf4j
public class LikesService {

    private final LikesRepository likesRepository;
    private final FeedRepository feedRepository;
    private final UserService userService;

    @Transactional
    public void like(LikeDTO likeDTO) {

        Feed feed = feedRepository.findById(likeDTO.getFeedId())
                .orElseThrow(() -> new FeedNotFoundException("존재하지 않는 " + likeDTO.getFeedId() + " 입니다."));
        Optional<Likes> likesByUserAndPost = likesRepository.findByFeedAndUserOrderByIdDesc(feed, userService.getCurrentUser());
        // .isPresent() : 해당 값이 null인지 아닌지 여부를 알려준다.
        if (likesByUserAndPost.isPresent() &&
                likesByUserAndPost.get().getLikeType()
                        .equals(likeDTO.getLikeType())) {
            throw new SpringRedditException("이미 해당 피드에 " +
                    likeDTO.getLikeType() + "좋아요를 눌렀습니다.");
        }
        if (UPLIKE.equals(likeDTO.getLikeType())) {
            feed.setLikeCount(feed.getLikeCount() + 1);
        } else {
            feed.setLikeCount(feed.getLikeCount() - 1);
        }
        likesRepository.save(mapToVote(likeDTO, feed));
        feedRepository.save(feed);
    }

    private Likes mapToVote(LikeDTO likeDTO, Feed feed) {
        return Likes.builder()
                .likeType(likeDTO.getLikeType())
                .feed(feed)
                .user(userService.getCurrentUser())
                .build();
    }
}
