package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.Post;
import com.reddit.redditcloneback.Service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(value = "/api/get")
@CrossOrigin(origins = "*")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

//    @GetMapping(value="/{id}")
//    public User userForm(@)

    @PostMapping(value = "/data/post/{username}", consumes = "application/json")
    public ResponseEntity<Void> postInsertDB(@RequestBody Post post, @PathVariable String username) {
        postService.savePost(post);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = "/data")
    public ResponseEntity<List<Post>> getPost() {
        return status(HttpStatus.OK).body(postService.findAllPost());
    }

    @GetMapping(value = "/hello")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<String> getString() {
        return ResponseEntity.ok("ksldjfksdjf");
    }
}
