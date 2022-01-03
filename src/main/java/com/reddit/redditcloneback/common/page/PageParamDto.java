package com.reddit.redditcloneback.common.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PageParamDto {
    private int page;
    private int size;
    private Map<String, Direction> sort = new HashMap<>();

    public void setPage(int page) {
        this.page = page <= 0 ? 1 : page;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setSort(Map<String, Direction> sort) {
        this.sort = sort;
    }

    /**
     * 정렬 (sort) 에 대한 정보가 들어있는지, 안들어있는지 확인하는 함수이다.
     * @return 정보가 들어있다면, 정렬을 포함하는 PageRequest 를 반환한다.
     */
    public PageRequest of() {
        if (sort.isEmpty())
            return PageRequest.of(page - 1, size);

        List<Sort.Order> orders = new ArrayList<>();

        sort.forEach((columnName, direction) -> {
            Sort.Order order = sort.get(columnName) == Direction.ASC ? Sort.Order.asc(columnName) : Sort.Order.desc(columnName);
            orders.add(order);
        });

        return PageRequest.of(page - 1, size, Sort.by(orders));
    }
}
