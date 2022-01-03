package com.reddit.redditcloneback.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.reddit.redditcloneback.model.Feed;
import lombok.*;

import java.time.ZonedDateTime;

public class FeedDto {
    public static class Request {
        @Data
        public static class Create {
            private String title;
            private String contents;
            private String nickname;
        }

        @Data
        public static class Modify {
            private String title;
            private String contents;
            private String uid;
        }

        @Data
        public static class Delete {
            private String uid;
            private String nickname;
        }
    }

    public static class Response {
        @Data
        @AllArgsConstructor
        public static class Success {
            private String uid;
            private String title;

            public static Success entityTo(Feed feed) {
                return new Success(
                        feed.getUid(),
                        feed.getTitle()
                );
            }
        }
        @Data
        @AllArgsConstructor
        public static class Modify {
            private String title;
            private String contents;
            private String uid;
            private String nickname;

            public static Modify dtoTo(QueryDsl.Detail detail) {
                return new Modify(
                        detail.title,
                        detail.contents,
                        detail.uid,
                        detail.nickname
                );
            }
        }
    }

    public static class QueryDsl {

        @Data
        public static class Detail {
            private String title;
            private String contents;
            private String uid;
            private Long likeCount;
            private Long commentCount;
            private String nickname;
            private String imagePath;
            private Time time;

            @Getter
            @NoArgsConstructor
            public static class Time {
                private Long createdDate;
                private Long modifiedDate;

                @QueryProjection
                public Time(ZonedDateTime createdDate, ZonedDateTime modifiedDate) {
                    this.createdDate = createdDate.toEpochSecond();
                    this.modifiedDate = modifiedDate.toEpochSecond();
                }
            }

            @QueryProjection
            public Detail(String title, String contents, String uid, Long likeCount, Long commentCount, String nickname, String imagePath, ZonedDateTime createdDate, ZonedDateTime modifiedDate) {
                this.title = title;
                this.contents = contents;
                this.uid = uid;
                this.likeCount = likeCount;
                this.commentCount = commentCount;
                this.nickname = nickname;
                this.imagePath = imagePath;
                this.time = new Time(createdDate, modifiedDate);
            }
        }
    }
}
