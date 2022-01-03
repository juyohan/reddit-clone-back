package com.reddit.redditcloneback.repository;

import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.custom.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    Optional<User> findByNickname(String nickname);

    @Query("select u from User u join fetch u.roles")
    Optional<User> findByEmailWithRoles(String email);
}
