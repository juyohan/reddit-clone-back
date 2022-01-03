package com.reddit.redditcloneback.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.reddit.redditcloneback.dto.QFeedDto_QueryDsl_Detail is a Querydsl Projection type for Detail
 */
@Generated("com.querydsl.codegen.ProjectionSerializer")
public class QFeedDto_QueryDsl_Detail extends ConstructorExpression<FeedDto.QueryDsl.Detail> {

    private static final long serialVersionUID = 1171536899L;

    public QFeedDto_QueryDsl_Detail(com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> contents, com.querydsl.core.types.Expression<String> uid, com.querydsl.core.types.Expression<Integer> likeCount, com.querydsl.core.types.Expression<String> nickname) {
        super(FeedDto.QueryDsl.Detail.class, new Class<?>[]{String.class, String.class, String.class, int.class, String.class}, title, contents, uid, likeCount, nickname);
    }

}

