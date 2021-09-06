package com.reddit.redditcloneback.DAO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

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
    @Column(name = "FEED_ID")
    private Long id;

    private String title;

    @Lob
    @Column(name = "FEED_DESC")
    private String desc;
    private String url;
//    private String path;

    // 이거 조인이 되어서 검색이 됌.
    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
    @JoinColumn(name = "USER_ID")
    private User user;

    // like_count의 기본 값을 0으로 설정
    @Column(name = "LIKE_COUNT", columnDefinition = "integer default 0")
    private Integer likeCount;

//    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
//    private List<Likes> likes = new ArrayList<>();

//    // 영속성을 하기 전에 likeCount의 값을 확인해서 null이라면 0으로 초기화 시켜준다.
//    @PrePersist
//    public void preLikeCount() {
//        this.likeCount = (this.likeCount == null) ? 0 : this.likeCount;
//    }

    // 연관관계 편의 메소드 ( 주인한테 설정 )
//    public void addUser(User user) {
//        this.user = user;
//        user.getFeeds().add(this);
//    }

//    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
//    private List<Comment> comments = new ArrayList<>();

}
