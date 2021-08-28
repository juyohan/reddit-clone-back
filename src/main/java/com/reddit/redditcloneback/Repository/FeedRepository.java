package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DAO.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findFeedsByUser(User user);

    // 2일 이전의 게시글들 중에서 likeCount보다 크거나 같은 게시글들 전부 가져옴
    List<Feed> findAllByCreateDateBetweenAndLikeCountIsGreaterThanEqual(LocalDateTime start, LocalDateTime end, Integer likeCount, Pageable pageable);

    List<Feed> findAllByCreateDateBetweenOrderByLikeCountDesc(LocalDateTime start, LocalDateTime end);

    Optional<Feed> findByUserAndId(User user, Long id);

    // like의 카운터 갯수로 출력
//    Optional<Feed> findAllBy
}
