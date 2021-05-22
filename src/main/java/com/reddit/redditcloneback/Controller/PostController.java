package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Post;
import com.reddit.redditcloneback.Post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(value = "/api/get" )
@CrossOrigin(origins = "*")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value = "/data/post", consumes = "application/json")
    public ResponseEntity<Void> postInsertDB(@RequestBody Post post) {
        System.out.println(post);
        postService.savePost(post);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/post/data")
    public ResponseEntity<List<Post>> getPost() {
        return status(HttpStatus.OK).body(postService.findAllPost());
    }
}
