package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.DTO.JwtTokenDTO;
import com.reddit.redditcloneback.DTO.LoginDTO;
import com.reddit.redditcloneback.DTO.UserDTO.RequestUserDTO;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.Error.UserNotEmailVerificationException;
import com.reddit.redditcloneback.JWT.JwtFilter;
import com.reddit.redditcloneback.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.IOException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody RequestUserDTO requestUserDTO) {
        String authKey = authService.signUp(requestUserDTO);
        return new ResponseEntity<>(authKey, OK);
    }

    // 이메일 인증
    @GetMapping(value = "/accountVerification")
    public RedirectView verifyAccount(@RequestParam("authKey") String authKey) {
        try {
            authService.verifyAccount(authKey);
            // 성공했을 경우 홈으로
            return new RedirectView("http://localhost:3000/");
        } catch (SpringRedditException exception) {
            System.out.println("exception = " + exception);
            // 실패하면 다시 이메일 인증하는 곳으로
            return new RedirectView("http://localhost:3000/error");
        }
    }

    // 아이디 중복 확인
    @PostMapping("/check/username")
    public ResponseEntity<String> checkUsername(@RequestBody RequestUserDTO requestUserDTO) {
        boolean result = authService.checkUsername(requestUserDTO.getUsername());
        if (result)
            return new ResponseEntity<>(requestUserDTO.getUsername(), OK);
        else
            return new ResponseEntity<>(null, NOT_ACCEPTABLE);
    }

    // email 중복 확인
    @PostMapping("/check/email")
    public ResponseEntity<String> checkEmail(@RequestBody RequestUserDTO requestUserDTO) {
        boolean result = authService.checkEmail(requestUserDTO.getEmail());
        if (result)
            return new ResponseEntity<>(requestUserDTO.getEmail(), OK);
        else
            return new ResponseEntity<>(null, NOT_ACCEPTABLE);
    }

    // 로그인
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            JwtTokenDTO jwtTokenDTO = authService.login(loginDTO);
            response.addHeader(JwtFilter.AUTHORIZATION_HEADER, jwtTokenDTO.getJwtToken());

            System.out.println("SecurityContextHolder = " + SecurityContextHolder.getContext().getAuthentication());
            System.out.println("jwtTokenDTO = " + jwtTokenDTO.getUsername());

            return new ResponseEntity<>(jwtTokenDTO, OK);
        } catch (BadCredentialsException badCredentialsException) {
            System.out.println("badCredentialsException = " + badCredentialsException);
            return new ResponseEntity<>(badCredentialsException, UNAUTHORIZED);
        }
    }

    // 비밀번호 찾기
    @PostMapping("/find/pw")
    public ResponseEntity<String> findPw(@RequestBody RequestUserDTO requestUserDTO) {
        boolean result = authService.checkEmail(requestUserDTO.getEmail());
        System.out.println("result = " + result);
        if (!result) {
            User user = authService.sendToMailFindPw(requestUserDTO.getEmail());
            System.out.println("AuthController.user = " + user);
            return new ResponseEntity<>("성공", OK);
        } else
            return new ResponseEntity<>("이메일 없움", FORBIDDEN);
    }

    // 카카오 로그인
    @GetMapping("/kakao")
    public ResponseEntity<String> getKakaoCode(@RequestParam("code") String code, HttpServletResponse response) {
        try {
            JwtTokenDTO jwtTokenDTO = authService.getKaKaoToken(code);
            response.addHeader(JwtFilter.AUTHORIZATION_HEADER, jwtTokenDTO.getJwtToken());
            return new ResponseEntity<String>(jwtTokenDTO.getUsername(), OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>("false", NOT_FOUND);
        } catch (UserNotEmailVerificationException e) {
            System.out.println("e = " + e);
            return new ResponseEntity<>("email not certification", UNAUTHORIZED);
        }
    }

    // 카카오 연동
    @PostMapping("/kakao/connect")
    public ResponseEntity<String> connectKakao(@RequestBody LoginDTO loginDTO, @RequestParam("kakaoId") String kakaoId) {
        System.out.println("loginDTO = " + loginDTO.toString());
//        String kakaoId = (String) request.getSession().getAttribute("kakaoId");
//        System.out.println("kakaoId = " + kakaoId);
        Boolean kakao = authService.connectKakao(loginDTO, kakaoId);
        if (kakao)
            return new ResponseEntity<>("Success", OK);
        else
            return new ResponseEntity<>("Fail", NOT_FOUND);
    }
}
