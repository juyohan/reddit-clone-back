package com.reddit.redditcloneback.model;

import lombok.*;

import javax.persistence.*;

//@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "files")
// 파일 자체를 DB에 저장하는게 아닌, 이름만 저장을 해준다.
// 파일은 별도의 서버에 파일을 저장한다.
public class FeedFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Feed feed;

    // 사용자 정의 메소드
    // feed를 저장을 하되, 저장하는 파일의 정보가 피드에 없다면 피드에 추가
    public void addFeed(Feed feed) {
        this.feed = feed;

//        if (!feed.getFiles().contains(this)) {
//            feed.getFiles().add(this);
//        }
    }

    @Column(name = "original_filename")
    private String originalFilename;
    @Column(name = "after_filename")
    private String afterFilename;
    private String path;
    private Long size;
    private String type;
}
