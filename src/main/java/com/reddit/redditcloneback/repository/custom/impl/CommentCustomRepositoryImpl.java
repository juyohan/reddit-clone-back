package com.reddit.redditcloneback.repository.custom.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reddit.redditcloneback.common.page.OrderByNull;
import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.dto.CommentDto;
import com.reddit.redditcloneback.dto.QCommentDto_QueryDsl_Detail;
import com.reddit.redditcloneback.model.Comment;
import com.reddit.redditcloneback.repository.custom.CommentCustomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import static com.reddit.redditcloneback.model.QComment.comment;
import static com.reddit.redditcloneback.model.QFeed.feed;
import static com.reddit.redditcloneback.model.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class CommentCustomRepositoryImpl
        extends QuerydslRepositorySupport
        implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Comment.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Long countCommentByUid(String uid) {
        return jpaQueryFactory.selectFrom(comment)
                .join(comment.feed, feed)
                .where(eqUid(uid))
                .fetchCount();
    }

    @Override
    public Page<PageDto> searchPageBy(PageParamDto pageParamDto, String uid) {
        PageRequest pageRequest = pageParamDto.of();

        QueryResults<CommentDto.QueryDsl.Detail> results = jpaQueryFactory.select(
                        new QCommentDto_QueryDsl_Detail(
                                comment.contents,
                                comment.user.nickname,
                                comment.user.imagePath,
                                comment.feed.uid,
                                comment.createdDate,
                                comment.modifiedDate
                        )
                ).from(comment)
                .join(comment.user, user)
                .join(comment.feed, feed)
                .where(eqUid(uid))
                .orderBy(comment.createdDate.desc())
                .offset(pageRequest.getOffset())
                .limit(Integer.toUnsignedLong(pageRequest.getPageSize()))
                .fetchResults();

        return new PageImpl(results.getResults(), pageRequest, results.getTotal());
    }

    private BooleanExpression eqUid(String uid) {
        return hasText(uid) ? comment.feed.uid.eq(uid) : null;
    }

}
