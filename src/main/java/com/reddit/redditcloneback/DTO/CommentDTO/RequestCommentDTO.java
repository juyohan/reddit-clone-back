package com.reddit.redditcloneback.DTO.CommentDTO;

import com.reddit.redditcloneback.DAO.Feed.Feed;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCommentDTO {
    private String content;
    private String key;
}
