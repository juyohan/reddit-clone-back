package com.reddit.redditcloneback.DAO.User;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "USER_PHOTO")
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_PHOTO_ID")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userPhoto")
    private User user;

    public void addUser(User user) {
        this.user = user;
        user.setUserPhoto(this);
    }

    private String originalFileName;
    private String afterFileName;
    private String filePath;
    private Long fileSize;
    private String fileType;

}
