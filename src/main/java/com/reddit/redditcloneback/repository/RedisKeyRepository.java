package com.reddit.redditcloneback.repository;

import com.reddit.redditcloneback.redis.RedisKey;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RedisKeyRepository extends CrudRepository<RedisKey, String> {
    @Override
    Optional<RedisKey> findById(String s);
}
