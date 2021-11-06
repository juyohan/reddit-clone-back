package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DAO.Feed.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query("SELECT c FROM Comment c join fetch c.feed")
    Page<Comment> findAllByFeed(Feed feed, Pageable pageable);

    List<Comment> findByFeedId(Long feedId);

    Page<Comment> findByFeedId(Long feedId, Pageable pageable);
}
