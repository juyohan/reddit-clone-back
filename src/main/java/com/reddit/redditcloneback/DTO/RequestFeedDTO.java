package com.reddit.redditcloneback.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFeedDTO {

    private String title;
    private String content;
    private String username;
    private List<MultipartFile> files;

}
