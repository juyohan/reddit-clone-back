//package com.reddit.redditcloneback.service;
//
//import com.reddit.redditcloneback.model.User;
//import com.reddit.redditcloneback.model.UserPhoto;
//import com.reddit.redditcloneback.repository.UserPhotoRepository;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//@Service
//@Slf4j
//@AllArgsConstructor
//public class UserPhotoService {
//
//    private final UserPhotoRepository userPhotoRepository;
//    private final UserService userService;
//    private final FeedFilesService feedFilesService;
//
//    public UserPhoto loadUserImage(MultipartFile multipartFile) {
//        UserPhoto userPhoto = new UserPhoto();
//
//        if (!multipartFile.isEmpty()) {
//            User user = userService.getCurrentUser();
//            userPhoto = (UserPhoto) feedFilesService.mappingFiles(multipartFile, "User/");
////            userPhoto.addUser(user);
//        }
//
//        return userPhotoRepository.save(userPhoto);
//    }
//}
