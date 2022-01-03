package com.reddit.redditcloneback.controller;

import com.reddit.redditcloneback.common.response.ApiResponse;
import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.redis.service.RedisService;
import com.reddit.redditcloneback.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.reddit.redditcloneback.common.response.ApiResponse.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RedisService redisService;

    /**
     *
     * @param create
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody UserDto.Request.Create create) {
        authService.signUp(create);
        return new ResponseEntity<>(success(), OK);
    }

    /**
     *
     * @param authKey
     * @return
     */
    @GetMapping("/accountVerification")
    public RedirectView verifyAccount(@Valid @RequestParam("authKey") String authKey) {
        if (redisService.verifyAccount(authKey))
            return new RedirectView("http://localhost:3000"); // 성공했을 경우 홈으로
        return new RedirectView("http://localhost:3000/error"); // 재인증 해달라는 요청
    }

    /**
     *
     * @param email
     * @param nickname
     * @param request
     * @return
     */
    @GetMapping("/check")
    public ResponseEntity<ApiResponse> check(
            @Valid @RequestParam(name = "email", required = false) String email,
            @Valid @RequestParam(name = "nickname", required = false) String nickname,
            HttpServletRequest request
    ) {
        boolean result = authService.checkUser(email, nickname, request);
        return new ResponseEntity<>(success(result), OK);
    }

    /**
     *
     * @param login
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody UserDto.Request.Login login) {
        UserDto.Response.LoginDetail loginDetail = authService.login(login);
        return new ResponseEntity<>(success(loginDetail), OK);
    }

//    // 비밀번호 찾기
//    @PostMapping("/find/pw")
//    public ResponseEntity<String> findPw(@RequestBody RequestUserDTO requestUserDTO) {
//        boolean result = authService.checkEmail(requestUserDTO.getEmail());
//        System.out.println("result = " + result);
//        if (!result) {
////            User user = authService.sendToMailFindPw(requestUserDTO.getEmail());
////            System.out.println("AuthController.user = " + user);
//            return new ResponseEntity<>("성공", OK);
//        } else
//            return new ResponseEntity<>("이메일 없움", FORBIDDEN);
//    }
}
