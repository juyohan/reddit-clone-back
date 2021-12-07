package com.reddit.redditcloneback.DTO.FeedDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.Like.LikeType;
import com.reddit.redditcloneback.DTO.CommentDTO.ResponseCommentDTO;
import com.reddit.redditcloneback.DTO.LikeDTO;
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
    private String imageUrl;

    public ResponseFeedDTO(Feed feed, List<String> fileNames) {
        this.feedId = feed.getId();
        this.feedContent = feed.getContent();
        this.title = feed.getTitle();
        this.likeCount = feed.getLikeCount();
        this.feedKey = feed.getKey();
        this.createDate = feed.getCreateDate();
        this.commentCount = feed.getComments().size();
        this.username = feed.getUser().getUsername();
        this.fileNames = fileNames;
        this.imageUrl = feed.getUser().getImageUrl();
//        this.likeType = likeDTO.getLikeType();
    }

}
