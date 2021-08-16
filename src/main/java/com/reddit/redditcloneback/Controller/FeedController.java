package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.Service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/")
    public ResponseEntity<List<Feed>> getHomePost() {
        return new ResponseEntity<List<Feed>>(feedService.findAllFeed(), HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<List<Feed>> getNewPost() {
        return new ResponseEntity<List<Feed>>(feedService.findAllFeed(), HttpStatus.OK);
    }
    @GetMapping("/hot")
    public ResponseEntity<List<Feed>> getHotPost() {
        return new ResponseEntity<List<Feed>>(feedService.hotFindFeeds(), HttpStatus.OK);
    }
    @GetMapping("/rising")
    public ResponseEntity<List<Feed>> getRisingPost() {
        return new ResponseEntity<List<Feed>>(feedService.findAllFeed(), HttpStatus.OK);
    }
    @GetMapping("/top")
    public ResponseEntity<List<Feed>> getTopPost() {
        return new ResponseEntity<List<Feed>>(feedService.findAllFeed(), HttpStatus.OK);
    }
}
