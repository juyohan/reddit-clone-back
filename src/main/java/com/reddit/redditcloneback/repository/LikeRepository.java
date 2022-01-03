package com.reddit.redditcloneback.repository;

import com.reddit.redditcloneback.model.Like;
import com.reddit.redditcloneback.repository.custom.LikeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long>, LikeCustomRepository {
}
