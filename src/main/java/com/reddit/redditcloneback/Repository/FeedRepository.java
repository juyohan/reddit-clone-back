package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findFeedsByUser(User user);
}
