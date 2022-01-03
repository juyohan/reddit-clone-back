package com.reddit.redditcloneback.repository.custom.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reddit.redditcloneback.dto.QUserDto_QueryDsl_EnableOfUser;
import com.reddit.redditcloneback.dto.QUserDto_QueryDsl_WithRole;
import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.custom.UserCustomRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.querydsl.core.group.GroupBy.*;
import static com.reddit.redditcloneback.model.QUser.user;
import static com.reddit.redditcloneback.model.QRole.role;
import static org.springframework.util.StringUtils.hasText;


@Repository
public class UserCustomRepositoryImpl
        extends QuerydslRepositorySupport
        implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public UserCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(User.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Optional<UserDto.QueryDsl.WithRole> searchWithRoleBy(String email, Long kakaoId) {
        return Optional.ofNullable(jpaQueryFactory
                .from(user)
                .innerJoin(user.roles, role)
                .where(
                        eqEmail(email),
                        eqKakaoId(kakaoId)
                )
                .transform(
                        groupBy(user.id)
                                .as(new QUserDto_QueryDsl_WithRole(
                                        user.nickname,
                                        user.email,
                                        user.imagePath,
                                        user.kakaoId,
                                        set(role.type),
                                        user.enable
                                ))
                ).get(1L)
        );
    }

    @Override
    public Optional<UserDto.QueryDsl.EnableOfUser> searchBy(String email, String nickname) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(new QUserDto_QueryDsl_EnableOfUser(
                                user.enable
                        ))
                        .from(user)
                        .where(
                                eqEmail(email),
                                eqNickname(nickname)
                        )
                        .fetchOne()
        );
    }

    @Override
    public void deleteUserByNickname(String nickname) {
        jpaQueryFactory.delete(user)
                .where(eqNickname(nickname))
                .execute();
    }

    @Override
    public void updateVerificationSuccessUser(String nickname) {
        jpaQueryFactory.update(user)
                .set(user.enable, true)
//                .set(user.roles, )
                .where(eqNickname(nickname))
                .execute();
    }

    private BooleanExpression eqNickname(String nickname) {
        return hasText(nickname) ? user.nickname.eq(nickname) : null;
    }

    private BooleanExpression eqEmail(String email) {
        return hasText(email) ? user.email.eq(email) : null;
    }

    private BooleanExpression eqKakaoId(Long kakaoId) {
        return kakaoId != null ? user.kakaoId.eq(kakaoId) : null;
    }

}
