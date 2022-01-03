package com.reddit.redditcloneback.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.Generated;

/**
 * com.reddit.redditcloneback.dto.QFeedDto_QueryDsl_Modify is a Querydsl Projection type for Modify
 */
@Generated("com.querydsl.codegen.ProjectionSerializer")
public class QFeedDto_QueryDsl_Modify extends ConstructorExpression<FeedDto.QueryDsl.Modify> {

    private static final long serialVersionUID = 1437965420L;

    public QFeedDto_QueryDsl_Modify(com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> contents, com.querydsl.core.types.Expression<String> uid) {
        super(FeedDto.QueryDsl.Modify.class, new Class<?>[]{String.class, String.class, String.class}, title, contents, uid);
    }

}

