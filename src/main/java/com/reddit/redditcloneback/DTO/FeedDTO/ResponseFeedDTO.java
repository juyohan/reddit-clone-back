package com.reddit.redditcloneback.DTO.FeedDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.Like.LikeType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFeedDTO {

    private Long feedId;
    private String username;
    private String feedContent;
    private String title;
    private Integer likeCount;
    private String feedKey;
    private LikeType likeType;
    private Integer commentCount;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createDate;
    private List<String> fileNames = new ArrayList<>();
    private String fileContentType;

    public ResponseFeedDTO(Feed feed, List<String> fileNames) {
        this.feedId = feed.getId();
        this.feedContent = feed.getFeedContent();
        this.title = feed.getTitle();
        this.likeCount = feed.getLikeCount();
        this.feedKey = feed.getFeedKey();
//        this.createDate = feed.getCreateDate();
//        this.commentCount = feed.getCommentCount();
        this.username = feed.getUser().getUsername();
        this.fileNames = fileNames;
    }
}
