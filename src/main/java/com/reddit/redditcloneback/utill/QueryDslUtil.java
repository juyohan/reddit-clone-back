package com.reddit.redditcloneback.utill;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtil {

    /**
     * 정렬에 대한 데이터가 존재한다면, 각 컬럼에 어떤 정렬을 할 것인지 결정을 해주는 함수이다.
     * @param sort 정렬의 정보가 담긴 데이터이다.
     * @param parent 어떤 클래스로 할 것인지에 대한 클래스 타입
     * @return OrderSpecifier 의 데이터들을 반환한다.
     */
    public static List<OrderSpecifier> getOrderSpecifier(
            Sort sort,
            Path<?> parent
    ) {
        List<OrderSpecifier> orders = new ArrayList<>();
        if (sort.isEmpty())
            return orders;

        sort.stream().forEach(order -> {
            Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
            orders.add(getSortedColumn(direction, parent, order.getProperty()));
        });
        return orders;
    }

    /**
     * 정렬의 대한 정보를 체크해서 OrderSpecifier 객체에 담는 함수이다.
     * @param order 정렬의 정보를 담은 데이터이다.
     * @param parent 어떤 클래스에 대한 정렬을 할 것인지 담은 클래스 타입이다.
     * @param fieldName 어떤 컬럼을 기준으로 정렬을 할 것인지 담은 컬럼 명이다.
     * @return 새로운 OrderSpecifier 객체를 생성해서 반환한다.
     */
    private static OrderSpecifier<?> getSortedColumn(
            Order order,
            Path<?> parent,
            String fieldName
    ) {
        if (fieldName == "random") {
            NumberTemplate<Double> template = Expressions.numberTemplate(Double.class, "RAND()");
            return new OrderSpecifier<>(order, template);
        }

        Path<Object> path = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, path);
    }
}
