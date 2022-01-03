package com.reddit.redditcloneback.model;

import com.reddit.redditcloneback.model.global.TimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@DynamicUpdate
@Table(
        name = "feeds",
        indexes = @Index(name = "idx_feeds_uid", columnList = "uid")
)
public class Feed extends TimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Lob
    private String contents;
    private String uid;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "like_count")
    private Long likeCount;
    @Column(name = "comment_count")
    private Long commentCount;
}
