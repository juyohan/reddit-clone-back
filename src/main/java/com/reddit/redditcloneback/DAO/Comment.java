package com.reddit.redditcloneback.DAO;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "COMMENT_CONTENT")
    private String content;

    // 시간도 넣어야됨.

//    @ManyToOne
//    @JoinColumn(name = "POST_ID")
//    private Feed feed;

//    public void addPost(Feed feed) {
//        this.feed = feed;
//        feed.getComments().add(this);
//    }
}
