package com.reddit.redditcloneback.DAO.Comment;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SubComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUB_COMMENT_ID")
    private Long id;


    private String subContent;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "COMMENT_ID")
//    private Comment comment;

//    private void addSubComment(Comment comment) {
//        this.comment = comment;
//        comment.getSubComments().add(this);
//    }
}
