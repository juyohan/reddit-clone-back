package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.Like.Likes;
import com.reddit.redditcloneback.DAO.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    Optional<Likes> findByFeedAndUserOrderByIdDesc(Feed feed, User user);

    // 가져온 게시글들에서 유저가 누른 정보들 가져옴
//    List<Likes> findByUserAndFeedIn(User user, List<Feed> feeds);

    Optional<Likes> findByUserAndFeedIn(User user, List<Feed> feeds);

}
