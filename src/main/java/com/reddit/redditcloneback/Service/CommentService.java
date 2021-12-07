package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.DTO.CommentDTO.RequestCommentDTO;
import com.reddit.redditcloneback.DTO.CommentDTO.ResponseCommentDTO;
import com.reddit.redditcloneback.DTO.Result;
import com.reddit.redditcloneback.Repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserService userService;
    private final FeedService feedService;

    public Comment saveComment(RequestCommentDTO requestCommentDTO) {
        User user = userService.getCurrentUser();
        if (user == null)
            log.info("로그인이 되어있는 아이디가 아닙니다.");
        Feed feed = feedService.searchWithFeedKey(requestCommentDTO.getKey());

        Comment comment = new Comment();
        comment.setContent(requestCommentDTO.getContent());
        comment.setUser(user);
        comment.addFeed(feed);
        comment.setCreateDate(LocalDateTime.now());

        return commentRepository.save(comment);
    }

    public Result getComment(Pageable pageable, String feedKey) {
        Feed feed = feedService.searchWithFeedKey(feedKey);
//        Page<Comment> comments = commentRepository.findAllByFeed(feed, pageable);
//        List<Comment> comments1 = commentRepository.findByFeedId(feed.getId());
        System.out.println("=====================================================");
//        Page<Comment> comments = commentRepository.findAll(pageable);
        Page<Comment> comments = commentRepository.findByFeedId(feed.getId(), pageable);
//        List<ResponseCommentDTO> responseCommentDTOS = mappingToCommentDTO(comments.getContent());
        List<ResponseCommentDTO> responseCommentDTOS = mappingToCommentDTO(comments.getContent(), feedKey);

//        return new Result(responseCommentDTOS, comments.getTotalPages());
        return new Result(responseCommentDTOS, comments.getTotalPages());
    }

    private List<ResponseCommentDTO> mappingToCommentDTO(List<Comment> commentList, String feedKey) {
        List<ResponseCommentDTO> responseCommentDTOS = commentList.stream().map(ResponseCommentDTO::new).collect(Collectors.toList());

        return responseCommentDTOS;
    }
}
