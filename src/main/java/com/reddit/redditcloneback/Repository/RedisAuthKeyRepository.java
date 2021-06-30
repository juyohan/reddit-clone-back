package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.RedisDAO.RedisAuthKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RedisAuthKeyRepository extends CrudRepository<RedisAuthKey, String> {
//    Optional<RedisAuthKey> findRedisAuthKeyById(String id);

    @Override
    Optional<RedisAuthKey> findById(String s);

    Optional<RedisAuthKey> deleteByUserUuid(String Uuid);

    Optional<RedisAuthKey> findByUserUuid(String Uuid);
}
