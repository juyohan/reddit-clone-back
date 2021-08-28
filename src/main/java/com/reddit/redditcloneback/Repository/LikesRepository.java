package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.Likes;
import com.reddit.redditcloneback.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByFeedAndUserOrderByIdDesc(Feed feed, User user);

//    Optional<Likes> findByUser(User user);

//    Optional<Likes> findByUser(User user);
    Optional<Likes> findByUser(User user);
}
