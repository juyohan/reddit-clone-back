package com.reddit.redditcloneback.DAO.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reddit.redditcloneback.DAO.BasicEntity;
import com.reddit.redditcloneback.DAO.Comment.Comment;
import lombok.*;


import javax.persistence.*;
import java.util.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User extends BasicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    @JsonIgnore
    private Long id;

    private String email;
    private String password;
    private String username;

    @Column(name = "kakao_id")
    private Long kakaoId;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "USER_PHOTO_ID")
//    private UserPhoto userPhoto;

    @Column(name = "image_url")
    private String imageUrl;

    // 필요 없을 수 있음.
//    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
//    private List<Comment> comment = new ArrayList<>();

//    @OneToOne(mappedBy = "user")
//    @JsonManagedReference
//    private VerificationToken verificationToken;
//
//    // 게시글과 유저 정보를 양방향으로 해야할까?
//    @Nullable
//    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
//    private List<Feed> feeds = new ArrayList<>();

    // 권한
    @ManyToMany
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY_NAME", referencedColumnName = "AUTHORITY_NAME")}
    )
    private Set<Authority> authorities = new HashSet<>();

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    // email 인증을 완료 했는지 안했는지 확인한다.
    private boolean enable;
}
