//package com.reddit.redditcloneback.Handler;
//
//import com.reddit.redditcloneback.DAO.File;
//import com.reddit.redditcloneback.Repository.FileRepository;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//import org.springframework.util.ObjectUtils;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//public class FileHandler {
//
//    private final FileRepository fileRepository;
//
//    public FileHandler(FileRepository fileRepository) {
//        this.fileRepository = fileRepository;
//    }
//
//    public List<File> parseFileInfo(
//            List<MultipartFile> multipartFiles
//    ) throws Exception {
//        List<File> photos = new ArrayList<>();
//
//        if (!CollectionUtils.isEmpty(multipartFiles)) {
//            LocalDateTime now = LocalDateTime.now();
//            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
//            String current_date = now.format(dateTimeFormatter);
//
//            String absolutePath = new java.io.File("").getAbsolutePath() + java.io.File.separator;
//
//            String path = "images" + java.io.File.separator + current_date;
//            java.io.File file = new java.io.File(path);
//
//            if (!file.exists()) {
//                boolean wasSuccessful = file.mkdirs();
//
//                if (!wasSuccessful)
//                    System.out.println("file : was not successful");
//            }
//
//            for (MultipartFile multipartFile : multipartFiles) {
//                String originalFileExtension;
//                String contentType = multipartFile.getContentType();
//
//                if (ObjectUtils.isEmpty(contentType))
//                    break;
//                else {
//                    if (contentType.contains("image/jpeg"))
//                        originalFileExtension = ".jpg";
//                    else if (contentType.contains("image/png"))
//                        originalFileExtension = ".png";
//                    else
//                        break;
//                }
//
//                String new_file_name = System.nanoTime() + originalFileExtension;
//
//
//            }
//
//        }
//    }
//}
