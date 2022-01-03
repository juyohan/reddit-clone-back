package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.jwt.JwtProvider;
import com.reddit.redditcloneback.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MailService mailService;
    private final UserService userService;
    private final RedisService redisService;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    /**
     * 회원가입을 진행하는 함수
     * DB 에 사용자를 저장시키고, 입력한 Email 인증을 위해 Redis 캐시 서버에 인증키를 저장한다.
     * @param create 회원가입을 요청한 객체 (email, password, nickname) 의 정보가 들어가있다.
     * @return 이메일 인증을 통해 사용된 인증 키를 반환한다. → 반환값이 없어도 상관없음.
     */
    public String signUp(UserDto.Request.Create create) {
        UserDto.Response.UserDetail user = userService.createUser(create);
        String authKey = redisService.setAuthKeyToRedis(user.getNickname()); // Key의 값을 전송받음
        return mailService.sendMail(user, authKey);
    }

    /**
     * 이메일과 닉네임의 중복 확인을 하기위한 함수
     * 사용자에게 요청이 들어왔을 때, 들어온 요청의 파라미터 이름을 통해 나누었다.
     * @param email 중복 확인을 하기위한 이메일
     * @param nickname 중복 확인을 하기위한 닉네임
     * @param request 요청이 들어온 정보
     * @return 중복된 데이터의 값이 없다면 true 를 반환한다.
     */
    public boolean checkUser(String email, String nickname, HttpServletRequest request) {
        while(!request.getParameterNames().nextElement().isEmpty()) {
            String key = request.getParameterNames().nextElement();
            if (key.equals("email"))
                userService.checkByEmailOrNickname(email, "");
            else if(key.equals("nickname"))
                userService.checkByEmailOrNickname("", nickname);
        }
        return true;
    }

    /**
     * 로그인을 처리하는 함수
     * Security Filter 중에서 UsernamePasswordAuthenticationToken Filter 를 통해,
     * 인증을 가져와 SecurityContextHolder 에 주입시키고 토큰을 생성한다.
     * @param login 로그인 요청이 온 데이터 객체 (email, password)
     * @return Filter 를 잘 거쳤다면, 토큰을 생성한 뒤, 로그인 한 사용자의 정보와 함께 반환한다.
     */
    public UserDto.Response.LoginDetail login(UserDto.Request.Login login) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.create(authentication);
    }

    public void deleteUser() {

    }

//    public Boolean connectKakao(LoginDTO loginDTO, String kakaoId) {
//        User user = userRepository.findByEmail(loginDTO.getEmail()).orElse(null);
//
//        if (user != null) {
//            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
//                user.setKakaoId(Long.parseLong(kakaoId));
//                userRepository.save(user);
//                return true;
//            }
//        }
//        return false;
//    }

    // 비밀번호 전송
//    public User sendToMailFindPw(String email) {
//        // emailCheck를 굳이 하지않고 여기서 orElseThrow 를 보내서 Error를 Catch 하는 방법으로 가자.
//        User user = userRepository.findOneWithAuthoritiesByEmail(email).orElseThrow(() ->
//                new RuntimeException(email + "을 찾을 수 없습니다.")
//        );
//
//        String temp = mailService.sendEmailFindPw(user);
//        user.setPassword(encodePassword(temp));
//
//        return userRepository.save(user);
//    }
}
