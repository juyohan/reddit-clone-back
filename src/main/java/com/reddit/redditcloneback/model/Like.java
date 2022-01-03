package com.reddit.redditcloneback.model;

import com.reddit.redditcloneback.model.enumeration.LikeType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicUpdate
@Table(name = "likes")
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private LikeType type;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "feed_id")
    private Feed feed;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;
}
