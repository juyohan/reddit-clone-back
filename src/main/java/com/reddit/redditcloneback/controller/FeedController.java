package com.reddit.redditcloneback.controller;

import com.reddit.redditcloneback.common.page.PageDto;
import com.reddit.redditcloneback.common.page.PageParamDto;
import com.reddit.redditcloneback.common.response.ApiResponse;
import com.reddit.redditcloneback.dto.FeedDto;
import com.reddit.redditcloneback.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.reddit.redditcloneback.common.response.ApiResponse.success;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FeedController {

    private final FeedService feedService;
//    private final FeedFilesService feedFilesService;

//    @PostMapping(value = "/save", consumes = {"multipart/form-data"})
//    public ResponseEntity<String> saveFeed(@RequestPart("feed-dto") RequestFeedDTO requestFeedDTO,
//                                           @RequestParam(value = "files", required = false) List<MultipartFile> multipartFileList
//                                         ) {
//
//        System.out.println("requestFeedDTO = " + requestFeedDTO);
//        Feed feed = feedService.createFeed(requestFeedDTO, multipartFileList);
//
//        return new ResponseEntity<>(feed.getKey(), HttpStatus.OK);
//    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveFeed(
            @RequestBody FeedDto.Request.Create feed
    ) {
        FeedDto.Response.Success success = feedService.create(feed);
        return new ResponseEntity<>(success(success), OK);
    }

    @PostMapping("/modify")
    public ResponseEntity<ApiResponse> modifyFeed(
            @RequestBody FeedDto.Request.Modify modify
    ) {
        FeedDto.Response.Success success = feedService.modifyFeed(modify);
        return new ResponseEntity<>(success(success), OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteFeed(
            @RequestBody FeedDto.Request.Delete delete
    ) {
        feedService.deleteOneFeed(delete);
        return new ResponseEntity<>(success(), OK);
    }

    @GetMapping("/{nickname}")
    public ResponseEntity<ApiResponse> getUserAllFeed(
            @RequestBody PageParamDto pageParamDto,
            @Valid @PathVariable("nickname") String nickname
    ) {
        PageDto page = feedService.findAllByNickname(pageParamDto, nickname);
        return new ResponseEntity<>(success(page), OK);
    }

    @GetMapping("/{nickname}/{uid}")
    public ResponseEntity<ApiResponse> getUserOneFeed(
            @Valid @PathVariable("nickname") String nickname,
            @Valid @PathVariable("uid") String uid
    ) {
        FeedDto.QueryDsl.Detail detail = feedService.findOneByUidAndNickname(nickname, uid);
        return new ResponseEntity<>(success(detail), OK);
    }

    @GetMapping("/{nickname}/{uid}/modify")
    public ResponseEntity<ApiResponse> modifyUserOneFeed(
            @Valid @PathVariable("nickname") String nickname,
            @Valid @PathVariable("uid") String uid
    ) {
        FeedDto.QueryDsl.Detail detail = feedService.findOneByUidAndNickname(nickname, uid);
        FeedDto.Response.Modify modify = FeedDto.Response.Modify.dtoTo(detail);
        return new ResponseEntity<>(success(modify), OK);
    }

    @GetMapping(path = {"/new", "/hot", "rising", "top"})
    public ResponseEntity<ApiResponse> getAllFeedByRequestPath(
            @RequestBody PageParamDto pageParamDto,
            HttpServletRequest request
    ) {
        PageDto page = feedService.findAllPageByRequestPath(pageParamDto, request);
        return new ResponseEntity<>(success(page), OK);
    }
}
