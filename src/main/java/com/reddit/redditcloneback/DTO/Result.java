package com.reddit.redditcloneback.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private T data;
    private int totalPage;

//    public Result(T f) {
//        this.data = feedPage;
//    }
}
