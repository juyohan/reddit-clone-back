package com.reddit.redditcloneback.repository.custom;

import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import org.springframework.data.domain.Page;

public interface CommentCustomRepository {
    Page<PageDto> searchPageBy(PageParamDto pageParamDto, String uid);

    Long countCommentByUid(String uid);
}
