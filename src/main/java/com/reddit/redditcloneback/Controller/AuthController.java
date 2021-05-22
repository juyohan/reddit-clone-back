package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.RegisterRequest;
import com.reddit.redditcloneback.Post.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup" )
    public ResponseEntity<String> signUp(@RequestBody RegisterRequest registerRequest) {
        System.out.println("================================");
        System.out.println(registerRequest);
        System.out.println("================================");
        authService.signUp(registerRequest);
        return new ResponseEntity<>("User Registration Successful", OK);
    }

    @GetMapping(value = "/accountVerification")
    public ResponseEntity<String> verifyAccount(@RequestParam("uid") String uid, @RequestParam("username") String username, @RequestParam("authKey") String authKey) {
        authService.verifyAccount(uid, username, authKey);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    @PostMapping("/check")
    public String checkUsername(@RequestBody String username) {
        System.out.println("username : " + username);
        int result = authService.checkUsername(username);
        System.out.println("result = " + result);
        if (result == 1)
            return "success";
        else
            return "fail";
    }
}
