package com.reddit.redditcloneback.repository.custom;

import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.dto.FeedDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface FeedCustomRepository {
    Optional<FeedDto.QueryDsl.Detail> searchOneByNicknameAndUid(String nickname, String uid);

    Page<PageDto> searchPageBy(PageParamDto pageParamDto, String nickname);
    Page<PageDto> searchPageByRequestPath(PageParamDto pageParamDto, Integer likeCount, Integer day);

    void updateLikeCountByUid(Long count, String uid);
    void updateCommentCountByUid(Long count, String uid);
    void deleteFeedByUidAndNickname(String uid, String nickname);
}
