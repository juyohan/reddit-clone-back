package com.reddit.redditcloneback.DAO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "POST_ID")
    private Long id;

    private String title;

    @Lob
<<<<<<< Updated upstream
    @Column(name = "POST_DESC")
=======
    @Column(name = "postDesc")
>>>>>>> Stashed changes
    private String desc;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    // 연관관계 편의 메소드 ( 주인한테 설정 )
//    public void addUser(User user) {
//        this.user = user;
//        user.getPosts().add(this);
//    }

}
