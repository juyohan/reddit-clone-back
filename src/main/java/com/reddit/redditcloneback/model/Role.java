package com.reddit.redditcloneback.model;

import com.reddit.redditcloneback.model.enumeration.RoleType;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "roles")
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private RoleType type;
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    public void addRole(User user) {
        this.user = user;

        if(!user.getRoles().contains(this)) {
            user.getRoles().add(this);
        }
    }
}
