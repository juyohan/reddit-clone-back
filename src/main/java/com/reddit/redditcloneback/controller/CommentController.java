package com.reddit.redditcloneback.controller;

import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.common.response.ApiResponse;
import com.reddit.redditcloneback.dto.CommentDto;
import com.reddit.redditcloneback.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.reddit.redditcloneback.common.response.ApiResponse.*;
import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping(value = "/api/comment")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveComment(
            @RequestBody CommentDto.Request.Create create
            ) {
        CommentDto.Response.Success success = commentService.saveComment(create);
        return new ResponseEntity<>(success(success), OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllComment(
            @RequestBody PageParamDto pageParamDto,
            @Valid @RequestParam("uid") String uid
    ) {
        PageDto comments = commentService.getComments(pageParamDto, uid);
        return new ResponseEntity<>(success(comments), OK);
    }

    @PostMapping("/modify")
    public ResponseEntity<ApiResponse> modifyComment(

    ) {

        return new ResponseEntity<>(success(), OK);
    }

    @GetMapping("/one")
    public ResponseEntity<ApiResponse> getOneComment(
            @Valid @RequestParam() String abc
    ) {

        return new ResponseEntity<>(success(), OK);
    }
}
