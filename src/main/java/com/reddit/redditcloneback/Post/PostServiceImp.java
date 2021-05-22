package com.reddit.redditcloneback.Post;

import com.reddit.redditcloneback.DAO.Post;

import java.util.List;

public interface PostServiceImp {
    public Post savePost(Post post);

    public List<Post> findAllPost();
}
