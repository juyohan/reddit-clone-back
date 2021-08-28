package com.reddit.redditcloneback.DAO;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

//    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID", referencedColumnName = "FEED_ID")
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private User user;

}
