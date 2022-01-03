package com.reddit.redditcloneback.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reddit.redditcloneback.dto.LikeDto;
import com.reddit.redditcloneback.dto.QLikeDto_QueryDsl_Detail;
import com.reddit.redditcloneback.model.Like;
import com.reddit.redditcloneback.model.enumeration.LikeType;
import com.reddit.redditcloneback.repository.custom.LikeCustomRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.reddit.redditcloneback.model.QFeed.feed;
import static com.reddit.redditcloneback.model.QLike.like;
import static com.reddit.redditcloneback.model.QUser.user;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class LikeCustomRepositoryImpl
        extends QuerydslRepositorySupport
        implements LikeCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LikeCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Like.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<LikeDto.QueryDsl.Detail> searchOneByUidAndNickname(String uid, String nickname) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(
                                new QLikeDto_QueryDsl_Detail(
                                        like.id,
                                        like.feed.uid,
                                        like.user.nickname,
                                        like.type
                                )
                        )
                        .from(like)
                        .join(like.feed, feed)
                        .join(like.user, user)
                        .where(
                                eqNickname(nickname),
                                eqUid(uid)
                        )
                        .fetchOne()
        );
    }

    @Override
    public void deleteLikeByLikeId(Long likeId) {
        jpaQueryFactory.delete(like)
                .where(eqLikeId(likeId))
                .execute();
    }

    @Override
    public void updateLikeTypeByLikeId(Long likeId, LikeType type) {
        jpaQueryFactory
                .update(like)
                .set(like.type, type)
                .where(eqLikeId(likeId))
                .execute();
    }

    @Override
    public Long countLikeOfFeed(String uid) {
        long up = jpaQueryFactory
                .select(like.type)
                .from(like)
                .join(like.feed, feed)
                .where(
                        eqUid(uid),
                        like.type.eq(LikeType.UP_LIKE)
                )
                .fetchCount();

        long down = jpaQueryFactory
                .select(like.type)
                .from(like)
                .join(like.feed, feed)
                .where(
                        eqUid(uid),
                        like.type.eq(LikeType.DOWN_LIKE)
                )
                .fetchCount();

        return up - down;
    }

    private BooleanExpression eqNickname(String nickname) {
        return hasText(nickname) ? like.user.nickname.eq(nickname) : null;
    }

    private BooleanExpression eqUid(String uid) {
        return hasText(uid) ? like.feed.uid.eq(uid) : null;
    }

    private BooleanExpression eqLikeId(Long likeId) {
        return likeId != null ? like.id.eq(likeId) : null;
    }
}
