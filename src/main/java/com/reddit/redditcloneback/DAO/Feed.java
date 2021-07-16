package com.reddit.redditcloneback.DAO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Feed extends BasicEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_ID")
    private Long id;

    private String title;

    @Lob
    @Column(name = "POST_DESC")
    private String desc;
    private String url;
//    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    // 연관관계 편의 메소드 ( 주인한테 설정 )
//    public void addUser(User user) {
//        this.user = user;
//        user.getFeeds().add(this);
//    }

//    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
//    private List<Comment> comments = new ArrayList<>();

//    @OneToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "LIKE_ID")
//    private Likes likes;
}
