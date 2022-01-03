package com.reddit.redditcloneback.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.reddit.redditcloneback.model.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.ZonedDateTime;

public class CommentDto {
    public static class Request {
        @Data
        public static class Create {
            private String contents;
            private String uid;
        }
    }

    public static class Response {
        @Data
        @AllArgsConstructor
        public static class Success {
            private String contents;
            private String nickname;
            private String uid;

            public static Success entityToDto(Comment comment) {
                return new Success(
                        comment.getContents(),
                        comment.getUser().getNickname(),
                        comment.getFeed().getUid()
                );
            }
        }

    }

    public static class QueryDsl {
        @Data
        public static class Detail {
            private String contents;
            private String nickname;
            private String imagePath;
            private String uid;
            private Time time;

            @Data
            public static class Time {
                private Long createdDate;
                private Long modifiedDate;

                public Time(ZonedDateTime createdDate, ZonedDateTime modifiedDate) {
                    this.createdDate = createdDate.toEpochSecond();
                    this.modifiedDate = modifiedDate.toEpochSecond();
                }
            }

            @QueryProjection
            public Detail(String contents, String nickname, String imagePath, String uid, ZonedDateTime createdDate, ZonedDateTime modifiedDate) {
                this.contents = contents;
                this.nickname = nickname;
                this.imagePath = imagePath;
                this.uid = uid;
                this.time = new Time(createdDate, modifiedDate);
            }
        }
    }
}
