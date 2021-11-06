package com.reddit.redditcloneback.DTO.FeedDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestFeedDTO {

    private String title;
    private String feedContent;
    private String username;

}
