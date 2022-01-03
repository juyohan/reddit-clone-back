//package com.reddit.redditcloneback.service;
//
//import com.reddit.redditcloneback.model.FeedFile;
//import com.reddit.redditcloneback.model.UserPhoto;
//import com.reddit.redditcloneback.repository.FeedFilesRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.net.URLDecoder;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class FeedFilesService {
//
//    private final FeedFilesRepository feedFilesRepository;
//
//    @Value("${spring.servlet.multipart.location}")
//    private String fileUploadDirectory;
//
//
//    // 피드에서 받은 파일들 저장
//    public List<FeedFile> loadFiles(List<MultipartFile> multipartFileList) {
//        List<FeedFile> reFiles = new ArrayList<>();
//
//        for (MultipartFile file : multipartFileList) {
//            if (!file.isEmpty()) {
//                FeedFile feedFile = (FeedFile) mappingFiles(file, "Feed/");
//
//                reFiles.add(feedFile);
//            }
//        }
//        return reFiles;
//    }
//
//    // 파일 하나씩 가져와서 저장
//    public Object mappingFiles(MultipartFile multipartFile, String path) {
//        // 한글로 된 파일 인코딩 처리한다.
//        String originalFilename = URLDecoder.decode(multipartFile.getOriginalFilename(), StandardCharsets.UTF_8);
//        // 원래 이름은 삭제하고 새로운 이름과 파일명을 합쳐준다.
//        String saveFileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf('.'));
//        String savePath = fileUploadDirectory + path + saveFileName;
//        try {
//            multipartFile.transferTo(new File(savePath));
//        } catch (IOException ioException) {
//            System.out.println("ioException = " + ioException);
//        }
//        if (path == "Feed/")
//            return  FeedFile.builder()
//                    .afterFilename(saveFileName)
//                    .originalFilename(originalFilename)
//                    .filePath(savePath)
//                    .fileSize(multipartFile.getSize())
//                    .fileType(multipartFile.getContentType())
//                    .build();
//        else
//            return UserPhoto.builder()
//                    .afterFileName(saveFileName)
//                    .originalFileName(originalFilename)
//                    .filePath(savePath)
//                    .fileSize(multipartFile.getSize())
//                    .fileType(multipartFile.getContentType())
//                    .build();
//    }
//
//    // 파일의 이미지들을 가져와 이름을 가져옴
//    public List<String> findFileNames(List<FeedFile> files) {
//        List<String> fileNames = files.stream().map(file -> {
//            return file.getAfterFilename();
//        }).collect(Collectors.toList());
//
//        return fileNames;
//    }
//
//    public byte[] convertByte(String fileName, String path) {
//        byte[] bytes = null;
//
//        try {
//            InputStream inputstream = new FileInputStream(fileUploadDirectory + path + fileName);
//            bytes = IOUtils.toByteArray(inputstream);
//            inputstream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException ioException) {
//            ioException.printStackTrace();
//        }
//
//        return bytes;
//    }
//
//    // 저장이 되어있는 파일과 변경되는 파일을 비교해서 같은거 선별
//    // 해당 피드 내에서 파일 DB에 같은 이름이 있는지 확인
//    public void findEqualFileAndModifyFile(List<FeedFile> files,
//                                           List<MultipartFile> multipartFiles) {
//
//    }
//
//    public void saveAnotherFile() {
//
//    }
//
//    private void deleteFile() {
//
//    }
//
//}
