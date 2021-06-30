package com.reddit.redditcloneback.RedisDAO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash(value = "authKey", timeToLive = 300)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisAuthKey {
    @Id
    private String id;

    private String userUuid;
}
