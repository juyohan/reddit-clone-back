package com.reddit.redditcloneback.repository;

import com.reddit.redditcloneback.model.Feed;
import com.reddit.redditcloneback.repository.custom.FeedCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedCustomRepository {
    Optional<Feed> findByUid(String uid);
}
