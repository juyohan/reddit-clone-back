package com.reddit.redditcloneback.repository.custom;

import com.reddit.redditcloneback.dto.LikeDto;
import com.reddit.redditcloneback.model.enumeration.LikeType;

import java.util.Optional;

public interface LikeCustomRepository {
    Optional<LikeDto.QueryDsl.Detail> searchOneByUidAndNickname(String uid, String nickname);

    void deleteLikeByLikeId(Long likeId);
    void updateLikeTypeByLikeId(Long likeId, LikeType type);

    Long countLikeOfFeed(String uid);
}
