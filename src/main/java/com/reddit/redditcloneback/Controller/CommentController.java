package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Comment.Comment;
import com.reddit.redditcloneback.DTO.CommentDTO.RequestCommentDTO;
import com.reddit.redditcloneback.DTO.CommentDTO.ResponseCommentDTO;
import com.reddit.redditcloneback.DTO.Result;
import com.reddit.redditcloneback.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.h2.jdbc.JdbcConnection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/comment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/save")
    public ResponseEntity<String> commentSave(@RequestBody RequestCommentDTO requestCommentDTO) {
        Comment comment = commentService.saveComment(requestCommentDTO);

        return new ResponseEntity(comment.getContent(), HttpStatus.OK);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<Result> commentGet(@RequestParam("feedKey") String key,
                                             @PageableDefault(size = 6, direction = Sort.Direction.DESC, sort = "CreateDate") Pageable pageable
    ) {
        Result result = commentService.getComment(pageable, key);

        return new ResponseEntity<Result>(result, HttpStatus.OK);
    }


}
