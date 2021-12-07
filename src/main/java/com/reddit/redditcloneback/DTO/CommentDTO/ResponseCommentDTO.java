package com.reddit.redditcloneback.DTO.CommentDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DAO.Comment.SubComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentDTO {

    private String username;
    private String content;
    private String key;
    private String imageUrl;
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    private LocalDateTime createDate;

//    public class comments {
//        private String username;
//        private String con
//    }

    public ResponseCommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.key = comment.getFeed().getKey();
        this.imageUrl = comment.getUser().getImageUrl();
        this.createDate = comment.getCreateDate();
    }
//    private List<SubComment> subComments = new ArrayList<>();

}
