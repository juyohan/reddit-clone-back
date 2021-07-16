package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.Service.FeedService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{username}")
    public ResponseEntity<List<Feed>> getUserAllFeed(@PathVariable String username) {
        return new ResponseEntity<>(feedService.allFeedsFindUser(username), HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<String> getNewPost() {
        return new ResponseEntity<>("hello",HttpStatus.OK);
    }
}
