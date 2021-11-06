package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.User.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    UserPhoto findById(long id);
}
