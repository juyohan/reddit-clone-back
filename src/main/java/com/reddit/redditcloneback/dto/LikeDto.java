package com.reddit.redditcloneback.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.reddit.redditcloneback.model.enumeration.LikeType;
import lombok.Data;

public class LikeDto {

    public static class Request {
        @Data
        public static class ClickLike {
            private LikeType type;
            private String uid;
        }
    }

    public static class Response {
    }

    public static class QueryDsl {
        @Data
        public static class Detail {
            private Long likeId;
            private String uid;
            private String nickname;
            private LikeType type;

            @QueryProjection
            public Detail(Long id, String uid, String nickname, LikeType type) {
                this.likeId = id;
                this.uid = uid;
                this.nickname = nickname;
                this.type = type;
            }
        }
    }
}
