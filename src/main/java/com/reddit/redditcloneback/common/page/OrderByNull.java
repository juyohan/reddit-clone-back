package com.reddit.redditcloneback.common.page;

import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

/**
 * 정렬을 할 때, Null 의 값이 존재하는 데이터는 제외하고 정렬을 해준다.
 */
public class OrderByNull extends OrderSpecifier {
    public static final OrderByNull DEFAULT = new OrderByNull();

    private OrderByNull() {
        super(Order.ASC, NullExpression.DEFAULT, NullHandling.Default);
    }
}
