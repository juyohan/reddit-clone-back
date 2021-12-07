package com.reddit.redditcloneback.DAO.Feed;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reddit.redditcloneback.DAO.BasicEntity;
import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DAO.User.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Feed extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEED_ID")
    private Long id;
    private String title;
    @Lob
    private String content;
    private String key;
//    private String path;

    // 이거 조인이 되어서 검색이 됌.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    // like_count의 기본 값을 0으로 설정
    @Column(name = "LIKE_COUNT", columnDefinition = "integer default 0")
    private Integer likeCount;

    @JsonIgnore
    @OneToMany(mappedBy = "feed", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<FeedFiles> files = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
