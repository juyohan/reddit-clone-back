package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.Repository.FileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class FileService {

    private final FileRepository fileRepository;


}
