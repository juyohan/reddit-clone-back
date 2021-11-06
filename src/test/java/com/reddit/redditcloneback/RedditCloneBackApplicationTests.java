package com.reddit.redditcloneback;

import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.Repository.CommentRepository;
import com.reddit.redditcloneback.Repository.FeedRepository;
import com.reddit.redditcloneback.Repository.UserRepository;
import com.reddit.redditcloneback.Service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedditCloneBackApplicationTests {

    @Autowired
    private final CommentService commentService;
    @Autowired
    private final CommentRepository commentRepository;

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    RedditCloneBackApplicationTests(CommentService commentService, CommentRepository commentRepository, UserRepository userRepository, FeedRepository feedRepository) {
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.feedRepository = feedRepository;
    }

    @Test
    public void saveComment() {
        User user = new User();
        user.setUsername("JUYOHAN");
        userRepository.save(user);

        Feed feed = new Feed();
        feed.setUser(user);
        feed.setContent("Content");
        feedRepository.save(feed);

        Comment comment = new Comment();
        comment.setContent("comment desu");

    }
}
