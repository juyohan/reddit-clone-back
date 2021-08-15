package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findOneWithAuthoritiesByEmailAndEnable(String email, boolean enable);

    Optional<User> findOneWithAuthoritiesByEmail(String email);

}
