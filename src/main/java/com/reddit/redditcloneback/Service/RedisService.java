package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.AuthKey.TempKey;
import com.reddit.redditcloneback.RedisDAO.RedisAuthKey;
import com.reddit.redditcloneback.Repository.RedisAuthKeyRepository;
import io.lettuce.core.RedisException;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisAuthKeyRepository redisAuthKeyRepository;

    public RedisService(RedisAuthKeyRepository redisAuthKeyRepository) {
        this.redisAuthKeyRepository = redisAuthKeyRepository;
    }

    // 이메일 인증을 위해 authKey 생성 후, Redis에 저장한다.
    public String setAuthKeyToRedis(String userUuid) {
        String authKey = new TempKey().getKey(50, false);

        RedisAuthKey redisAuthKey = RedisAuthKey.builder()
                .id(authKey)
                .userUuid(userUuid)
                .build();

        redisAuthKeyRepository.save(redisAuthKey);

        return authKey;
    }

    public void deleteAuthKeyToRedis(String userUuid) {
        RedisAuthKey redisAuthKey = redisAuthKeyRepository.findByUserUuid(userUuid).orElseThrow(
                () -> new RedisException("존재하지 않는 값 : " + userUuid));

        redisAuthKeyRepository.delete(redisAuthKey);
    }
}
