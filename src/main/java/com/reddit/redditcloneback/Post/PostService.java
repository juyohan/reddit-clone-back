package com.reddit.redditcloneback.Post;

import com.reddit.redditcloneback.DAO.Post;
import com.reddit.redditcloneback.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService implements PostServiceImp{

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> findAllPost() {
        return postRepository.findAll();
    }
}
