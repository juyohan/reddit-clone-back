package com.reddit.redditcloneback.repository.custom.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reddit.redditcloneback.common.page.OrderByNull;
import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.dto.FeedDto;
import com.reddit.redditcloneback.dto.QFeedDto_QueryDsl_Detail;
import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.repository.custom.FeedCustomRepository;
import com.reddit.redditcloneback.utill.QueryDslUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.reddit.redditcloneback.model.QFeed.feed;
import static com.reddit.redditcloneback.model.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class FeedCustomRepositoryImpl
        extends QuerydslRepositorySupport
        implements FeedCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public FeedCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Feed.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<FeedDto.QueryDsl.Detail> searchOneByNicknameAndUid(String nickname, String uid) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                new QFeedDto_QueryDsl_Detail(
                                        feed.title,
                                        feed.contents,
                                        feed.uid,
                                        feed.likeCount,
                                        feed.commentCount,
                                        feed.user.nickname,
                                        feed.user.imagePath,
                                        feed.createdDate,
                                        feed.modifiedDate
                                )
                        )
                        .from(feed)
                        .join(feed.user, user)
                        .where(
                                eqNickname(nickname),
                                eqUid(uid)
                        )
                        .fetchOne()
        );
    }

    @Override
    public Page<PageDto> searchPageBy(PageParamDto pageParamDto, String nickname) {
        PageRequest pageRequest = pageParamDto.of();

        QueryResults<FeedDto.QueryDsl.Detail> results = jpaQueryFactory
                .select(
                        new QFeedDto_QueryDsl_Detail(
                                feed.title,
                                feed.contents,
                                feed.uid,
                                feed.likeCount,
                                feed.commentCount,
                                feed.user.nickname,
                                feed.user.imagePath,
                                feed.createdDate,
                                feed.modifiedDate
                        )
                )
                .from(feed)
                .join(feed.user, user)
                .where(
                        eqNickname(nickname)
                )
                .orderBy(
                        QueryDslUtil.getOrderSpecifier(pageRequest.getSort(), feed)
                                .stream()
                                .toArray(OrderSpecifier[]::new)
                )
                .offset(pageRequest.getOffset())
                .limit(Integer.toUnsignedLong(pageRequest.getPageSize()))
                .fetchResults();

        return new PageImpl(results.getResults(), pageRequest, results.getTotal());
    }

    @Override
    public Page<PageDto> searchPageByRequestPath(PageParamDto pageParamDto, Integer likeCount, Integer day) {
        PageRequest pageRequest = pageParamDto.of();

        QueryResults<FeedDto.QueryDsl.Detail> results = jpaQueryFactory
                .select(
                        new QFeedDto_QueryDsl_Detail(
                                feed.title,
                                feed.contents,
                                feed.uid,
                                feed.likeCount,
                                feed.commentCount,
                                feed.user.nickname,
                                feed.user.imagePath,
                                feed.createdDate,
                                feed.modifiedDate
                        )
                )
                .from(feed)
                .join(feed.user, user)
                .where(
                        goeLikeCount(likeCount),
                        betweenCreateDate(day)
                )
                .orderBy(
                        QueryDslUtil.getOrderSpecifier(pageRequest.getSort(), feed)
                                .stream()
                                .toArray(OrderSpecifier[]::new)
                )
                .offset(pageRequest.getOffset())
                .limit(Integer.toUnsignedLong(pageRequest.getPageSize()))
                .fetchResults();

        return new PageImpl(results.getResults(), pageRequest, results.getTotal());
    }

    @Override
    public void updateLikeCountByUid(Long count, String uid) {
        jpaQueryFactory.update(feed)
                .set(feed.likeCount, count)
                .where(eqUid(uid))
                .execute();
    }

    @Override
    public void deleteFeedByUidAndNickname(String uid, String nickname) {
        jpaQueryFactory.delete(feed)
                .where(
                        eqUid(uid),
                        eqNickname(nickname)
                )
                .execute();
    }

    @Override
    public void updateCommentCountByUid(Long count, String uid) {
        jpaQueryFactory.update(feed)
                .set(feed.commentCount, count)
                .where(eqUid(uid))
                .execute();
    }

    private BooleanExpression eqUid(String uid) {
        return hasText(uid) ? feed.uid.eq(uid) : null;
    }

    private BooleanExpression eqNickname(String nickname) {
        return hasText(nickname) ? feed.user.nickname.eq(nickname) : null;
    }

    private BooleanExpression goeLikeCount(Integer likeCount) {
        return likeCount != null ? feed.likeCount.goe(likeCount) : null;
    }

    private BooleanExpression betweenCreateDate(Integer day) {
        return day != null ? feed.createdDate.between(ZonedDateTime.now().minusDays(day), ZonedDateTime.now()) : null;
    }
}
