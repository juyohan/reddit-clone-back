package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.dto.CommentDto;
import com.reddit.redditcloneback.model.Comment;
import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;
    private final FeedService feedService;


    /**
     * 댓글에 대한 정보를 DB 에 저장시키는 함수이다.
     * 댓글을 작성했을 때, 댓글의 개수를 업데이트 시켜준다.
     * @param create 어떤 게시글에 누가 댓글을 달았는지 담은 객체이다. (uid, contents)
     * @return 제대로 저장이 되었다면 저장시킨 댓글의 정보를 DTO 로 변환하고 반환한다.
     */
    @Transactional
    public CommentDto.Response.Success saveComment(CommentDto.Request.Create create) {
        User user = userService.getCurrentUser();
        Feed feed = feedService.findOneByUid(create.getUid());

        Comment comment = commentRepository.save(
                Comment.builder()
                        .user(user)
                        .feed(feed)
                        .contents(create.getContents())
                        .build()
        );

        Long count = commentRepository.countCommentByUid(create.getUid());
        feedService.updateCommentCount(count, create.getUid());

        return CommentDto.Response.Success.entityToDto(comment);
    }

    /**
     * 게시글에 담긴 댓글의 정보를 페이징 처리를 통해 가져오는 함수이다.
     * @param pageParamDto 페이징에 대한 정보가 담긴 객체이다. (sort, size, page)
     * @param uid 게시글의 고유 번호
     * @return 페이징 처리가 된 데이터를 DTO 타입으로 변환시키고 반환한다.
     */
    public PageDto getComments(PageParamDto pageParamDto, String uid) {
        Page<PageDto> page = commentRepository.searchPageBy(pageParamDto, uid);

        return PageDto.toDto(page);
    }
}
