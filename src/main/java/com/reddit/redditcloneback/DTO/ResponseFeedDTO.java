package com.reddit.redditcloneback.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reddit.redditcloneback.DAO.LikeType;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseFeedDTO {

    private Long id;
    private String username;
    private String desc;
    private String title;
    private Integer likeCount;
    private String url;
//    private boolean upLike;
//    private boolean downLike;
    private LikeType likeType;
//    private Integer commentCount;
//    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createDate;
}
