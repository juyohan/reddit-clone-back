package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DTO.LikeDTO;
import com.reddit.redditcloneback.Service.LikesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/likes")
@AllArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<Void> likes(@RequestBody LikeDTO likeDTO) {
        likesService.like(likeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
