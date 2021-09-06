package com.reddit.redditcloneback.DTO;

import com.reddit.redditcloneback.DAO.LikeType;
import com.reddit.redditcloneback.DAO.Likes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {

    private Long feedId;

    private LikeType likeType;

    public LikeDTO(Likes likes) {
        this.feedId = likes.getFeed().getId();
        this.likeType = likes.getLikeType();
    }
}
