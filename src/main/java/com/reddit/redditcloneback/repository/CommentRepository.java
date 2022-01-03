package com.reddit.redditcloneback.repository;

import com.reddit.redditcloneback.model.Comment;
import com.reddit.redditcloneback.repository.custom.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
}
