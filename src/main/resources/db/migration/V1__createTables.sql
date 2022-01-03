CREATE TABLE `users`
(
    `id`         bigint                   NOT NULL AUTO_INCREMENT COMMENT '사용자 id',
    `email`      varchar(256)             NOT NULL COMMENT '사용자 이메일',
    `password`   varchar(1024)            NOT NULL COMMENT '비밀번호',
    `image_path` varchar(512)             NOT NULL COMMENT '사용자 프로필 사진',
    `kakao_id`   bigint     DEFAULT NULL COMMENT '카카오 고유 번호',
    `nickname`   varchar(256)             NOT NULL COMMENT '사용자 닉네임',
    `enable`     tinyint(0) DEFAULT FALSE NOT NULL COMMENT '인증 완료 여부',
    PRIMARY KEY (`id`),
    CONSTRAINT `idx_users_nickname` UNIQUE (`nickname`),
    CONSTRAINT `idx_users_kakao_id` UNIQUE (`kakao_id`),
    UNIQUE KEY `ux_users_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARACTER
      SET = utf8mb4
    COMMENT
        '사용자 정보';

CREATE TABLE `roles`
(
    `id`      bigint      NOT NULL AUTO_INCREMENT COMMENT '권한 id',
    `type`    varchar(36) NOT NULL COMMENT '권한 이름',
    `user_id` bigint      NOT NULL COMMENT '사용자 id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
    COMMENT '권한 정보';

CREATE TABLE `feeds`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '게시글의 id 값',
    `title`         varchar(512) NOT NULL COMMENT '게시글의 제목',
    `contents`      blob         NOT NULL COMMENT '게시글의 내용',
    `uid`           varchar(128) NOT NULL COMMENT '게시글의 고유 번호',
    `like_count`    bigint       NOT NULL COMMENT '좋아요의 개수',
    `comment_count` bigint       NOT NULL COMMENT '댓글의 개수',
    `created_date`  datetime     NOT NULL COMMENT '게시글이 작성된 시간',
    `modified_date` datetime DEFAULT NULL COMMENT '게시글이 변경된 시간',
    `user_id`       bigint       NOT NULL COMMENT '게시글 올린 사용자의 id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`),
    CONSTRAINT `idx_feeds_uid` UNIQUE (`uid`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
    COMMENT '게시글의 정보';

CREATE TABLE `likes`
(
    `id`      bigint      NOT NULL AUTO_INCREMENT COMMENT '좋아요의 id 값',
    `type`    varchar(36) NOT NULL COMMENT '버튼의 타입',
    `feed_id` bigint      NOT NULL COMMENT '게시글의 id',
    `user_id` bigint      NOT NULL COMMENT '사용자의 id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`feed_id`) REFERENCES feeds (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
    COMMENT '좋아요의 정보';

CREATE TABLE `comments`
(
    `id`            bigint       NOT NULL AUTO_INCREMENT COMMENT '댓글의 id',
    `contents`      varchar(512) NOT NULL COMMENT '댓글의 내용',
    `created_date`  datetime     NOT NULL COMMENT '댓글이 작성된 시간',
    `modified_date` datetime DEFAULT NULL COMMENT '댓글이 변경된 시간',
    `user_id`       bigint       NOT NULL COMMENT '사용자의 id',
    `feed_id`       bigint       NOT NULL COMMENT '게시글의 id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES users (`id`),
    FOREIGN KEY (`feed_id`) REFERENCES feeds (`id`)
) ENGINE = InnoDB
  DEFAULT CHARACTER SET = utf8mb4
    COMMENT '댓글의 정보';

CREATE TABLE `files`
(
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '파일의 id',
    `feed_id` bigint NOT NULL COMMENT '게시글의 id',
    PRIMARY KEY (`id`),
    FOREIGN KEY (`feed_id`) REFERENCES feeds (`id`)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COMMENT '파일의 정보';
