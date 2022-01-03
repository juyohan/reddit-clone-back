package com.reddit.redditcloneback.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.reddit.redditcloneback.dto.QUserDto_QueryDsl_WithRole is a Querydsl Projection type for WithRole
 */
@Generated("com.querydsl.codegen.ProjectionSerializer")
public class QUserDto_QueryDsl_WithRole extends ConstructorExpression<UserDto.QueryDsl.WithRole> {

    private static final long serialVersionUID = -1919460991L;

    public QUserDto_QueryDsl_WithRole(com.querydsl.core.types.Expression<String> nickname, com.querydsl.core.types.Expression<String> email, com.querydsl.core.types.Expression<String> imagePath, com.querydsl.core.types.Expression<Long> kakaoId, com.querydsl.core.types.Expression<? extends java.util.Set<com.reddit.redditcloneback.model.enumeration.RoleType>> roleTypes, com.querydsl.core.types.Expression<Boolean> enable) {
        super(UserDto.QueryDsl.WithRole.class, new Class<?>[]{String.class, String.class, String.class, long.class, java.util.Set.class, boolean.class}, nickname, email, imagePath, kakaoId, roleTypes, enable);
    }

}

