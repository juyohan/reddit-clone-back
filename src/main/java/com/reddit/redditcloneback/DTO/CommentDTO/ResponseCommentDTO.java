package com.reddit.redditcloneback.DTO.CommentDTO;

import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DAO.Comment.SubComment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCommentDTO {

    private String username;
    private String content;

    public ResponseCommentDTO(Comment comment) {
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
    }
//    private List<SubComment> subComments = new ArrayList<>();

}
