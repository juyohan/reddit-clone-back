package com.reddit.redditcloneback.DAO.Like;

import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.User.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "LIKES_ID")
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private LikeType likeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID", referencedColumnName = "FEED_ID")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

}
