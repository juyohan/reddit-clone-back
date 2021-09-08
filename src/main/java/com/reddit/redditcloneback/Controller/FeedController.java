package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.DTO.Result;
import com.reddit.redditcloneback.Service.FeedService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "*")
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping("/save")
    public ResponseEntity<Feed> saveFeed(@RequestBody FeedDTO feedDTO) {
        Feed feed = feedService.saveFeed(feedDTO);
        return new ResponseEntity<>(feed, HttpStatus.OK);
    }

    @GetMapping("/modify")
    public ResponseEntity<Feed> modifiedFeed(@RequestBody FeedDTO feedDTO) {
        Feed feed = feedService.modifiedFeed(feedDTO);
        return new ResponseEntity<>(feed, HttpStatus.OK);
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<List<Feed>> getUserAllFeed(@PathVariable String username) {
//        return new ResponseEntity<>(feedService.allFeedsFindUser(username), HttpStatus.OK);
//    }

    @GetMapping("/new")
    public ResponseEntity<Result> getNewPost(@PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = "CreateDate") Pageable pageable) {
        return new ResponseEntity<>(feedService.newFindFeeds(pageable), HttpStatus.OK);
    }
    @GetMapping("/hot")
    public ResponseEntity<Result> getHotPost(@PageableDefault(size = 25) Pageable pageable) {
        return new ResponseEntity<>(feedService.hotFindFeeds(pageable), HttpStatus.OK);
    }
    @GetMapping("/rising")
    public ResponseEntity<Result> getRisingPost(@PageableDefault(size = 25) Pageable pageable) {
        return new ResponseEntity<>(feedService.risingFindFeeds(pageable), HttpStatus.OK);
    }
    @GetMapping(value = {"/top", "/"})
    public ResponseEntity<Result> getTopPost(@PageableDefault(size = 25, direction = Sort.Direction.DESC, sort = "likeCount") Pageable pageable) {
        return new ResponseEntity<>(feedService.topFindFeeds(pageable), HttpStatus.OK);
    }
}
