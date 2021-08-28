package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.LikeType;
import com.reddit.redditcloneback.DTO.LikeDTO;
import com.reddit.redditcloneback.Service.LikesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/likes")
@AllArgsConstructor
public class LikesController {
    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<Integer> likes(@RequestBody LikeDTO likeDTO) {
        Integer likeCount = likesService.like(likeDTO);
        return new ResponseEntity<>(likeCount, OK);
    }

    @PostMapping("/get")
    public ResponseEntity<LikeType> getLikes(@RequestBody LikeDTO likeDTO) {
        LikeType likeType = likesService.checkLikeToFeed(likeDTO);
        return new ResponseEntity<>(likeType, OK);
    }
}
