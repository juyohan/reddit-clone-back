package com.reddit.redditcloneback.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.LikeType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFeedDTO {

    private Long feedId;
    private String username;
    private String desc;
    private String title;
    private Integer likeCount;
    private String url;
    private LikeType likeType;
//    private Integer commentCount;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createDate;


    public ResponseFeedDTO(Feed feed) {
        this.feedId = feed.getId();
        this.desc = feed.getDesc();
        this.title = feed.getTitle();
        this.likeCount = feed.getLikeCount();
        this.url = feed.getUrl();
        this.createDate = feed.getCreateDate();
//        this.commentCount = feed.getCommentCount();
        this.username = feed.getUser().getUsername();
    }
}
