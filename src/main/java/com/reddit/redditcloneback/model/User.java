package com.reddit.redditcloneback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reddit.redditcloneback.model.enumeration.RoleType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@DynamicUpdate // 전제 Update 를 하는게 아니라, 필요한 부분만 update 되게끔
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "ux_users_email", columnNames = "email"),
        indexes = {
                @Index(name = "idx_users_nickname", columnList = "nickname"),
                @Index(name = "idx_users_kakao_id", columnList = "kakao_id")
        }
)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @JsonIgnore
    private String password;
    private String nickname;
    @Column(name = "kakao_id")
    private Long kakaoId;
    @Column(name = "image_path")
    private String imagePath;
    private boolean enable;
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Role> roles = new HashSet<>();
}
