package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.JwtTokenDTO;
import com.reddit.redditcloneback.DTO.LoginDTO;
import com.reddit.redditcloneback.DTO.UserDTO;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.JWT.JwtFilter;
import com.reddit.redditcloneback.Service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.net.URI;
import java.net.URL;

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
    public ResponseEntity<String> signUp(@Valid @RequestBody UserDTO userDTO) {
        String authKey = authService.signUp(userDTO);
            return new ResponseEntity<>(authKey, OK);
    }

    // 이메일 인증
    @GetMapping(value = "/accountVerification")
    public RedirectView verifyAccount(@RequestParam("authKey") String authKey) {
        try {
            authService.verifyAccount(authKey);
            // 성공했을 경우 홈으로
            return new RedirectView("http://localhost:8080/");
        } catch (SpringRedditException exception) {
            System.out.println("exception = " + exception);
            // 실패하면 다시 이메일 인증하는 곳으로
            return new RedirectView("http://localhost:8080/error");
        }
    }

    // 아이디 중복 확인
    @PostMapping("/check/username")
    public ResponseEntity<String> checkUsername(@RequestBody UserDTO userDTO) {
        boolean result = authService.checkUsername(userDTO.getUsername());
        if (result)
            return new ResponseEntity<>(userDTO.getUsername(), OK);
        else
            return new ResponseEntity<>(null, NOT_ACCEPTABLE);
    }

    // email 중복 확인
    @PostMapping("/check/email")
    public ResponseEntity<String> checkEmail(@RequestBody UserDTO userDTO) {
        boolean result = authService.checkEmail(userDTO.getEmail());
        if (result)
            return new ResponseEntity<>(userDTO.getEmail(), OK);
        else
            return new ResponseEntity<>(null, NOT_ACCEPTABLE);
    }

    // 로그인
//    @PostMapping(value = "/login")
//    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO) {
//        try {
//            JwtTokenDTO jwtTokenDTO = authService.login(loginDTO);
//
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtTokenDTO.getJwtToken());
//
//            return new ResponseEntity<>(jwtTokenDTO, OK);
//        }  // UserDetailsService에서 던진 Exception을 받아서 여기서 처리한다.
//        catch (RuntimeException runtimeException) {
//            System.out.println("받은 예외 = " + runtimeException);
//            return new ResponseEntity<>(loginDTO.getEmail(), UNAUTHORIZED);
//        }
//    }

    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        try {
            JwtTokenDTO jwtTokenDTO = authService.login(loginDTO);

//            final Cookie cookie = new Cookie("auth", jwtTokenDTO.getJwtToken());
//            cookie.setMaxAge(60*30);
//            cookie.setHttpOnly(true);
//            response.addCookie(cookie);
//
            response.addHeader(JwtFilter.AUTHORIZATION_HEADER, jwtTokenDTO.getJwtToken());

//            response.setHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtTokenDTO.getJwtToken());

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//            httpServletResponse.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtTokenDTO.getJwtToken());
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(JwtFilter.AUTHORIZATION_HEADER + "AAAAA", "Bearer " + jwtTokenDTO.getJwtToken());

            return new ResponseEntity<>(jwtTokenDTO, OK);
        }
//        catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
//            System.out.println("internalAuthenticationServiceException = " + internalAuthenticationServiceException);
//            return new ResponseEntity<>(internalAuthenticationServiceException, FORBIDDEN);
//        }
        // UserDetailsService에서 던진 Exception을 받아서 여기서 처리한다.
        catch (BadCredentialsException badCredentialsException) {
            System.out.println("badCredentialsException = " + badCredentialsException);
            return new ResponseEntity<>(badCredentialsException, UNAUTHORIZED);
        }
    }

    @PostMapping("/find/pw")
    public ResponseEntity<String> findPw(@RequestBody UserDTO userDTO) {
        boolean result = authService.checkEmail(userDTO.getEmail());
        System.out.println("result = " + result);
        if (!result) {
            User user = authService.sendToMailFindPw(userDTO.getEmail());
            System.out.println("AuthController.user = " + user);
            return new ResponseEntity<>("성공", OK);
        }
        else
            return new ResponseEntity<>("이메일 없움", FORBIDDEN);
    }



//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<User> getMyUserInfo() {
//        return ResponseEntity.ok(authService.getMyUserWithAuthorities().get());
//    }
//
//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN','USER')")
//    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(authService.getUserWithAuthorities(username).get());
//    }
}
