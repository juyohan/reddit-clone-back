package com.reddit.redditcloneback.redis.service;

import com.reddit.redditcloneback.common.key.TempKey;
import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.redis.RedisKey;
import com.reddit.redditcloneback.repository.RedisKeyRepository;
import com.reddit.redditcloneback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisKeyRepository redisKeyRepository;
    private final UserService userService;

    /**
     * 이메일 인증을 위해 authKey 생성 후, Redis 에 저장하는 함수이다.
     * @param nickname 이메일 인증을 요청한 사용자의 닉네임
     * @return 생성된 인증 키 (authKey) 를 반환한다.
     */
    public String setAuthKeyToRedis(String nickname) {
        String authKey = new TempKey().getKey(50, false);

        RedisKey redisRedisKey = RedisKey.builder()
                .id(authKey)
                .nickname(nickname)
                .build();

        redisKeyRepository.save(redisRedisKey);

        return authKey;
    }

    /**
     * 이메일 인증을 통해 들어온 인증 키의 값이 Redis 에 존재하는지, 확인하는 함수이다.
     * @param authKey 인증 키에 대한 데이터
     * @return 회원가입에 성공했다면 true 를 반환한다.
     */
    public boolean verifyAccount(String authKey) {
        RedisKey redisKey = redisKeyRepository.findById(authKey).orElseThrow(
                () -> new BusinessException(Exceptions.CACHE_EXPIRED));
        // TODO :  만료가 되었을 때, 다시 재인증 메일을 보낼 수 있도록.

        return userService.successSignup(redisKey.getNickname());
    }
}
