package com.reddit.redditcloneback.Controller;

import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.DTO.JwtTokenDTO;
import com.reddit.redditcloneback.DTO.LoginDTO;
import com.reddit.redditcloneback.DTO.UserDTO;
import com.reddit.redditcloneback.JWT.JwtFilter;
import com.reddit.redditcloneback.Service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
    public ResponseEntity<String> verifyAccount(@RequestParam("authKey") String authKey) {
        authService.verifyAccount(authKey);
        return new ResponseEntity<>("Account Activated Successfully", OK);
    }

    // 아이디 중복 확인
    @PostMapping("/check")
    public boolean checkUsername(@RequestBody String username) {
        boolean result = authService.checkUsername(username);
        System.out.println("result = " + result);
        return result;
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
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        try {
            JwtTokenDTO jwtTokenDTO = authService.login(loginDTO);

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//            httpServletResponse.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwtTokenDTO.getJwtToken());
//            HttpHeaders headers = new HttpHeaders();
//            headers.add(JwtFilter.AUTHORIZATION_HEADER + "AAAAA", "Bearer " + jwtTokenDTO.getJwtToken());

            return new ResponseEntity<>(jwtTokenDTO, OK);
        }  // UserDetailsService에서 던진 Exception을 받아서 여기서 처리한다.
        catch (RuntimeException runtimeException) {
            System.out.println("받은 예외 = " + runtimeException);
            return new ResponseEntity<>(loginDTO.getEmail(), UNAUTHORIZED);
        }
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
