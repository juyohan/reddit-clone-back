//package com.reddit.redditcloneback.controller;
//
//import com.reddit.redditcloneback.model.UserPhoto;
//import com.reddit.redditcloneback.service.FeedFilesService;
//import com.reddit.redditcloneback.service.UserPhotoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.ResourceRegion;
//import org.springframework.http.*;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/file")
//public class FileController {
//
//    @Value("${spring.servlet.multipart.location}")
//    private String filesUploadDirectory;
//
//    private final FeedFilesService feedFilesService;
//    private final UserPhotoService userPhotoService;
//
//    // User의 프로필 사진 보기
//    @GetMapping(value = "/user/image",
//            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
//    )
//    public ResponseEntity<byte[]> viewUserImage(@RequestParam("fileName") String fileName) {
//        byte[] bytes = feedFilesService.convertByte(fileName, "User/");
//
//        return new ResponseEntity<byte[]>(bytes,HttpStatus.OK);
//    }
//
//    // User의 프로필 사진 저장
//    @PostMapping("/user/save")
//    public ResponseEntity<String> saveUserImage(@RequestParam("files") MultipartFile multipartFile) {
//        UserPhoto userPhoto = userPhotoService.loadUserImage(multipartFile);
//
//        return new ResponseEntity<String>(userPhoto.getAfterFileName(), HttpStatus.OK);
//    }
//
//    // 이미지 접근
//    @GetMapping(value = "/image",
//            produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE}
//    )
//    public ResponseEntity<byte[]> viewImage(@RequestParam("fileName") String fileName) {
//        byte[] bytes = feedFilesService.convertByte(fileName, "Feed/");
//
//        return new ResponseEntity<>(bytes, HttpStatus.OK);
//    }
//
//    // 동영상 접근
//    @GetMapping(value = "video")
//    public ResponseEntity<ResourceRegion> viewVideo(@RequestParam("fileName") String fileName,
//                                                    @RequestHeader HttpHeaders httpHeaders
//                                                    ) throws IOException {
//         Resource resource = new FileSystemResource(filesUploadDirectory + "Feed/" + fileName);
//
//        final long chunkSize = 1024 * 1024 * 1;
//        ResourceRegion region = null;
//        long videoLength = resource.contentLength();
//        long length = 0;
//
//        try {
//            HttpRange httpRange = httpHeaders.getRange().stream().findFirst().get();
//            long start = httpRange.getRangeStart(videoLength);
//            long end = httpRange.getRangeEnd(videoLength);
//            length = end - start + 1;
//            long rangeLength = Long.min(chunkSize, length);
//            region = new ResourceRegion(resource, start, rangeLength);
//        } catch (Exception e) {
//            System.out.println("e = " + e);
//        }
//
//        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).cacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
//                .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
//                .header("Accept-Ranges", "bytes")
//                .eTag(fileName)
//                .body(region);
//    }
//
//}
