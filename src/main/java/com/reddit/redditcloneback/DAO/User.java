package com.reddit.redditcloneback.DAO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    private String email;
    private String password;
    private String username;

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
