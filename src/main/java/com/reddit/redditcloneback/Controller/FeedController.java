package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Feed;
import com.reddit.redditcloneback.DTO.FeedDTO;
import com.reddit.redditcloneback.DTO.ResponseFeedDTO;
import com.reddit.redditcloneback.Service.FeedService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/all")
    public ResponseEntity<Page<Feed>> getAllFeed(Pageable pageable) {
        return new ResponseEntity<>(feedService.findAllFeed(pageable),HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<Feed>> getHomePost() {
        return new ResponseEntity<>(feedService.risingFindFeeds(), HttpStatus.OK);
    }

    @GetMapping("/new")
    public ResponseEntity<List<ResponseFeedDTO>> getNewPost() {
        return new ResponseEntity<>(feedService.newFindFeeds(), HttpStatus.OK);
    }
    @GetMapping("/hot")
    public ResponseEntity<List<Feed>> getHotPost(@PageableDefault(size = 5) Pageable pageable) {
        return new ResponseEntity<List<Feed>>(feedService.hotFindFeeds(pageable), HttpStatus.OK);
    }
    @GetMapping("/rising")
    public ResponseEntity<List<Feed>> getRisingPost() {
        return new ResponseEntity<List<Feed>>(feedService.risingFindFeeds(), HttpStatus.OK);
    }
    @GetMapping("/top")
    public ResponseEntity<List<ResponseFeedDTO>> getTopPost(@PageableDefault(size = 5, direction = Sort.Direction.DESC, sort = "likeCount") Pageable pageable) {
        return new ResponseEntity<>(feedService.topFindFeeds(pageable), HttpStatus.OK);
    }
}
