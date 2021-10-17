package com.reddit.redditcloneback.DTO;


import com.reddit.redditcloneback.DAO.Feed;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Result<T> {
    private T feed;
    private int totalPage;

    public Result(T feedPage) {
        this.feed = feedPage;
    }
}
