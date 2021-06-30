package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.Service.UserService;
import com.reddit.redditcloneback.Util.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<User> getUserInfo() {
        User user = userService.loginAfterFindUserName();
        return ResponseEntity.ok(user);
    }

}
