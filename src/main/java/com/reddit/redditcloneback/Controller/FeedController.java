package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DTO.FeedDTO.RequestFeedDTO;
import com.reddit.redditcloneback.DTO.FeedDTO.ResponseModifyFeedDTO;
import com.reddit.redditcloneback.DTO.Result;
import com.reddit.redditcloneback.Service.FeedService;
import com.reddit.redditcloneback.Service.FeedFilesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
@CrossOrigin(origins = "*")
public class FeedController {

    private final FeedService feedService;
    private final FeedFilesService feedFilesService;

    public FeedController(FeedService feedService, FeedFilesService feedFilesService) {
        this.feedService = feedService;
        this.feedFilesService = feedFilesService;
    }

    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
    public ResponseEntity<String> saveFeed(@RequestPart("feed-dto") RequestFeedDTO requestFeedDTO,
                                           @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList
                                         ) {
//    public ResponseEntity<String> saveFeed(@RequestBody RequestFeedDTO requestFeedDTO
//    ){
        System.out.println("requestFeedDTO = " + requestFeedDTO);
//        Feed feed = feedService.createFeed(requestFeedDTO);
        Feed feed = feedService.createFeed(requestFeedDTO, multipartFileList);


        return new ResponseEntity<>(feed.getKey(), HttpStatus.OK);
    }

    @GetMapping("/modify")
    public ResponseEntity<ResponseModifyFeedDTO> modifiedFeed(@RequestParam("feedKey") String key) {
        ResponseModifyFeedDTO responseModifyFeedDTO = feedService.mappingModifyFeedDTO(key);
        return new ResponseEntity<>(responseModifyFeedDTO, HttpStatus.OK);
    }

    @PostMapping("/modify/save")
    public ResponseEntity<String> modifiedSaveFeed(@RequestPart("feed-dto") RequestFeedDTO requestFeedDTO,
                                                   @RequestParam(value = "files") List<MultipartFile> multipartFiles,
                                                   @RequestParam("feedKey") String feedKey) {
        feedService.modifiedSaveFeed(feedKey, requestFeedDTO, multipartFiles);


        return new ResponseEntity<String>(HttpStatus.OK);
    }

//    @GetMapping("/{username}")
//    public ResponseEntity<List<Feed>> getUserAllFeed(@PathVariable String username) {
//        return new ResponseEntity<>(feedService.allFeedsFindUser(username), HttpStatus.OK);
//    }

    @GetMapping("/new")
    public ResponseEntity<Result> getNewPost(
            @PageableDefault(size = 8, direction = Sort.Direction.DESC, sort = "CreateDate") Pageable pageable
    ) {
        return new ResponseEntity<>(feedService.newFindFeeds(pageable), HttpStatus.OK);
    }

    @GetMapping(value = {"/hot", "/"})
    public ResponseEntity<Result> getHotPost(@PageableDefault(size = 8) Pageable pageable) {
        return new ResponseEntity<>(feedService.hotFindFeeds(pageable), HttpStatus.OK);
    }

    @GetMapping("/rising")
    public ResponseEntity<Result> getRisingPost(@PageableDefault(size = 8) Pageable pageable) {
        return new ResponseEntity<>(feedService.risingFindFeeds(pageable), HttpStatus.OK);
    }

    @GetMapping("/top")
    public ResponseEntity<Result> getTopPost(@PageableDefault(size = 8, direction = Sort.Direction.DESC, sort = "likeCount") Pageable pageable) {
        return new ResponseEntity<>(feedService.topFindFeeds(pageable), HttpStatus.OK);
    }
}
