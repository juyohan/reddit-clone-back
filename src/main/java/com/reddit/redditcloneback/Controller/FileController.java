package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DTO.FeedDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/file")
public class FileController {

    @Value("${spring.servlet.multipart.location}")
    private final String fileUploadDirectory;

    // Servlet 을 사용해서 파일을 저장
    @PostMapping("/upload")
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
                String downloadPath = fileUploadDirectory + part.getSubmittedFileName();

                log.info("파일 저장경로 : {} ", downloadPath);

                part.write(downloadPath);
            }
        }
        return "upload-form";
    }

    // spring 을 사용해서 파일을 저장
    @PostMapping("/upload2")
    public String saveFile2(@RequestPart("feed-dto") FeedDTO feedDTO,
                            @RequestParam("file") List<MultipartFile> multipartFiles,
                            HttpServletRequest request) throws IOException {

        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                String downloadPath = fileUploadDirectory + multipartFile.getOriginalFilename();
                // 파일 저장
                multipartFile.transferTo(new File(downloadPath));
            }
        }

        return "upload-form";
    }
}
