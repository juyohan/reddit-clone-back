package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.dto.LikeDto;
import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.model.Like;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final FeedService feedService;
    private final UserService userService;

    /**
     * 좋아요에 대한 버튼을 클릭했을 때, DB 에 저장을 하거나, 업데이트를 하거나, 삭제를 하는 함수이다.
     * 좋아요에 대한 정보가 없으면 DB 에 생성을 해주고 존재한다면,
     * 업데이트 (기존 DB 에 저장되어 있는 정보와 다르다면) 를 하거나 삭제 (기존 DB 에 저장되어 있는 정보와 같다면) 를 해준다.
     * @param clickLike 어떤 버튼을 눌렀는지에 대한 정보가 담긴 객체이다. (LikeType, uid, nickname)
     * @return 게시글의 총 좋아요의 개수를 합해서 반환한다.
     */
    @Transactional
    public Long checkLikedOfFeed(LikeDto.Request.ClickLike clickLike) {
        LikeDto.QueryDsl.Detail like = likeRepository
                .searchOneByUidAndNickname(clickLike.getUid(), userService.getCurrentUserNickname())
                .orElse(null);

        if (like != null) {
            if (like.getType().equals(clickLike.getType())) {
                likeRepository.deleteLikeByLikeId(like.getLikeId());
            } else
                likeRepository.updateLikeTypeByLikeId(like.getLikeId(), clickLike.getType());
        } else {
            createLike(clickLike);
        }
        Long count = likeRepository.countLikeOfFeed(clickLike.getUid());
        feedService.updateLikeCount(count, clickLike.getUid());

        return count;
    }

    /**
     * DB 에 좋아요의 정보를 저장시키는 함수이다.
     * @param clickLike 어떤 게시글에 좋아요를 클릭했는지에 대한 데이터가 담긴 객체이다. (uid, nickname, LikeType)
     */
    private void createLike(
            LikeDto.Request.ClickLike clickLike
    ) {
        User user = userService.getCurrentUser();
        Feed feed = feedService.findOneByUid(clickLike.getUid());

        likeRepository.save(
                Like.builder()
                        .type(clickLike.getType())
                        .feed(feed)
                        .user(user)
                        .build()
        );
    }
}
