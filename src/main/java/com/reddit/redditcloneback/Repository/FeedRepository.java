package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findFeedsByUser(User user);

    Optional<Feed> findByUserAndId(User user, Long id);

    // like의 카운터 갯수로 출력
//    Optional<Feed> findAllBy
}
