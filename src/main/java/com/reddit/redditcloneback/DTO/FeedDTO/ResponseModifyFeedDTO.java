package com.reddit.redditcloneback.DTO.FeedDTO;


import com.reddit.redditcloneback.DAO.Feed.FeedFiles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseModifyFeedDTO {

    private String title;
    private String feedContent;
    // 엔티티 그대로 넘기는건 좋지 않아보여...
    private List<FeedFiles> files = new ArrayList<>();
}
