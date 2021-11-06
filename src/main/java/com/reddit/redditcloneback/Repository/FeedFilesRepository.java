package com.reddit.redditcloneback.Repository;

import com.reddit.redditcloneback.DAO.Feed.Feed;
import com.reddit.redditcloneback.DAO.Feed.FeedFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedFilesRepository extends JpaRepository<FeedFiles, Long> {

    List<FeedFiles> findByFeedIn(List<Feed> feeds);


}
