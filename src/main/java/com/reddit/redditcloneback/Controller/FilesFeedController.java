package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DTO.FeedDTO.RequestFeedDTO;
import com.reddit.redditcloneback.Repository.FeedFilesRepository;
import com.reddit.redditcloneback.Service.FeedFilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.String;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/feed/files")
public class FilesFeedController {

    @Value("${spring.servlet.multipart.location}")
    private String filesUploadDirectory;

    private final FeedFilesService feedFilesService;
    private final FeedFilesRepository feedFilesRepository;

    // Servlet 을 사용해서 파일을 저장
    @PostMapping("/upload3")
    public String saveFile(HttpServletRequest request) throws ServletException, IOException {
        log.info("request : {}", request);

        String sampleStringData = request.getParameter("sampleStringData");

        log.info("sampleStringData : {}", sampleStringData);

        Collection<Part> partCollection = request.getParts();

        for (Part part : partCollection) {
            Collection<String> headerNames = part.getHeaderNames();

            for (String headerName : headerNames) {
                log.info("header {} : {}", headerName, part.getHeaderNames());
            }

            InputStream inputStream = part.getInputStream();
            StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String downloadPath = filesUploadDirectory + part.getSubmittedFileName();

                log.info("파일 저장경로 : {} ", downloadPath);

                part.write(downloadPath);
            }
        }
        return "upload-form";
    }

    @GetMapping(value = "/download2",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_PNG_VALUE}
    )
    public ResponseEntity<byte[]> useSearch(@RequestParam("fileName") String fileName) {
        byte[] bytes = null;

        try {
            InputStream inputStream = new FileInputStream(filesUploadDirectory + fileName);
            bytes =IOUtils.toByteArray(inputStream);
            inputStream.close();
        } catch (IOException e) {
            System.out.println("e = " + e);
        }
        return new ResponseEntity<byte[]>(bytes, HttpStatus.OK);
    }

    @GetMapping(value = "/download3")
    public ResponseEntity<Long> useSearchMovie(@RequestParam("fileName") List<String> fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] bytes = null;
        Long size = 0L;
        for (String name : fileName) {
//            size = name.length();
//            System.out.println("name = " + name);
//            System.out.println("name.length() = " + size);
//            InputStream inputStream = new FileInputStream(filesUploadDirectory + name);
            File file = new File(filesUploadDirectory + name);
            long rangeStart = 0L;
            long rangeEnd = 0L;
            boolean isPart = false;

            try{
                size = file.length();
                // range 헤더의 기본 형식은 'bytes={start}-{end}' 이다.
                String range = request.getHeader("range");

                if (range != null) {
                    if(range.endsWith("-")) {
                        range += (size - 1);
                    }
                    int idx = range.trim().indexOf("-");
                    rangeStart = Long.parseLong(range.substring(6, idx));
                    rangeEnd = Long.parseLong(range.substring(idx+1));

                    if (rangeStart > 0)
                        isPart = true;
                } else {
                    rangeStart = 0;
                    rangeEnd = size - 1;
                }

                long partSize = rangeEnd - rangeStart + 1;

                response.reset();

                response.setStatus(isPart ? 206 : 200);
                response.setContentType("video/mp4");
                response.addHeader("Content-Range", "bytes " + rangeStart + "-" + rangeEnd + "/" + size);
                response.addHeader("Accept-Ranges", "bytes");
                response.addHeader("Content-Length", ""+partSize);

                OutputStream outputStream = response.getOutputStream();

//                file.seek


            } catch (IOException e) {
                System.out.println("e = " + e);
            }
            System.out.println("file.length() = " + file.length());
//            bytes = IOUtils.toByteArray(inputStream);
//            inputStream.close();
        }
        return new ResponseEntity<Long>(size, HttpStatus.OK);
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> download(@PathVariable String fileName, HttpServletRequest request) throws MalformedURLException {
//        FilesDTO filesDTO = filesRepository.findById(fileId).or

//        Resource resource = new FileSystemResource(fileUploadDirectory + fileName);

        Path path = Paths.get(filesUploadDirectory);
        Path filePath = path.resolve(fileName).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists())
            log.info("Error 났음");

        String contentType = null;

        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (contentType == null)
            contentType = "application/octet-stream";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // spring 을 사용해서 파일을 저장
    @PostMapping("/upload2")
    public String saveFile2(@RequestPart("feed-dto") RequestFeedDTO requestFeedDTO,
                            @RequestParam("file") List<MultipartFile> multipartFiles,
                            HttpServletRequest request) throws IOException {
        log.info("feed-dto = {}", requestFeedDTO);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "image/png");

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                String downloadPath = filesUploadDirectory + multipartFile.getOriginalFilename();
                log.info("content-type = {}", multipartFile.getContentType());
                log.info("fileName = {} ", multipartFile.getOriginalFilename());
                log.info("fileSize = {} ", multipartFile.getSize());
                // 파일 저장
                multipartFile.transferTo(new File(downloadPath));
            }
        }

        return "upload-form";
    }

    @PostMapping("/user/upload/photo")
    public String uploadUserPhoto(@RequestParam("user-photo") MultipartFile multipartFile,
                                  HttpServletRequest httpServletRequest) {
//        filesService


        if (!multipartFile.isEmpty()) {

        }

        return "success";
    }

    @PostMapping("/upload")
    public String uploadFiles(@RequestPart("file-upload") RequestFeedDTO requestFeedDTO) {


        return "success";
    }

}
