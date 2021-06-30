package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DAO.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {
    Optional<VerificationToken> findByUuid(String uuid);

    Optional<VerificationToken> findByUser(User user);
}
