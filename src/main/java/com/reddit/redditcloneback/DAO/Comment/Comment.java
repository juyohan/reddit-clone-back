package com.reddit.redditcloneback.DAO.Comment;

import com.reddit.redditcloneback.DAO.BasicEntity;
import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.User.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BasicEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "COMMENT_CONTENT")
    private String content;

    // 시간도 넣어야됨.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    public void addUser(User user) {
        this.user = user;
        user.getComment().add(this);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    public void addFeed(Feed feed) {
        this.feed = feed;
        feed.getComments().add(this);
    }

//    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY)
//    private List<SubComment> subComments = new ArrayList<>();
}
