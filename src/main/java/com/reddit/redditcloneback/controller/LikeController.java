package com.reddit.redditcloneback.controller;

import com.reddit.redditcloneback.common.response.ApiResponse;
import com.reddit.redditcloneback.dto.LikeDto;
import com.reddit.redditcloneback.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.reddit.redditcloneback.common.response.ApiResponse.success;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // 좋아요를 눌렀을 때
    @PostMapping("/click")
    public ResponseEntity<ApiResponse> clickLike(
            @RequestBody LikeDto.Request.ClickLike like
            ) {
        Long likeCount = likeService.checkLikedOfFeed(like);
        return new ResponseEntity<>(success(likeCount), OK);
    }
}
