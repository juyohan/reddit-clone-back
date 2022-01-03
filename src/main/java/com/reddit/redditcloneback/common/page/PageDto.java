package com.reddit.redditcloneback.common.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@AllArgsConstructor
@Data
public class PageDto {
    private List<?> content;
    private int page;
    private int size;
    private int numberOfElement;
    private Long totalElements;
    private int totalPages;

    /**
     * Page 내부에 있는 데이터를 내가 원하는 데이터로 가공시키는 함수이다.
     * @param page Page<> 형식으로 되어있는 객체타입
     * @return
     */
    public static PageDto toDto(Page<?> page) {
        return new PageDto(
                page.getContent(),
                page.getNumber() + 1,
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
