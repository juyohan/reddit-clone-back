package com.reddit.redditcloneback.DAO;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
// 파일 자체를 DB에 저장하는게 아닌, 이름만 저장을 해준다.
// 파일은 별도의 서버에 파일을 저장한다.
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FEED_FILE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    private String originalFileName;

    private String filePath;

    private Long fileSize;

}
