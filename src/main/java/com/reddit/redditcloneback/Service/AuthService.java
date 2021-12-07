package com.reddit.redditcloneback.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reddit.redditcloneback.DAO.User.Authority;
import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.DAO.User.VerificationToken;
import com.reddit.redditcloneback.DTO.CustomUserDetails;
import com.reddit.redditcloneback.DTO.JwtTokenDTO;
import com.reddit.redditcloneback.DTO.kakaoDTO.KakaoProfileDTO;
import com.reddit.redditcloneback.DTO.kakaoDTO.KakaoTokenDTO;
import com.reddit.redditcloneback.DTO.LoginDTO;
import com.reddit.redditcloneback.DTO.UserDTO.RequestUserDTO;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.Error.UserNotEmailVerificationException;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.RedisDAO.RedisAuthKey;
import com.reddit.redditcloneback.Repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static java.time.LocalDateTime.*;


@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final RedisAuthKeyRepository redisAuthKeyRepository;

    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtProvider jwtProvider;

    // 회원가입
    public String signUp(RequestUserDTO requestUserDTO) {
        if (userRepository.findOneWithAuthoritiesByEmail(requestUserDTO.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 존재한 유저입니다.");
        }

        User user = new User();
        user.setUsername(requestUserDTO.getUsername());
        user.setEmail(requestUserDTO.getEmail());
        user.setPassword(encodePassword(requestUserDTO.getPassword()));
        user.setCreateDate(now());
        user.setEnable(false);
        user.setKakaoId(null);
        user.setImageUrl("http://localhost:8080/api/file/user/image?fileName=init.png");

        userRepository.save(user);

        String uidToken = generateVerificationToken(user);
        return mailService.sendMail(user, uidToken);
    }

    // 토큰 생성
    private String generateVerificationToken(User user) {
        String uidToken = UUID.randomUUID().toString(); // 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setUuid(uidToken);

        verificationTokenRepository.save(verificationToken);

        return uidToken;
    }

    // 비밀번호 인코딩
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    // 이메일 인증을 시도한 uuid가 db에 있는지 확인한다.
    public void verifyAccount(String authKey) {
        // 가져온 authKey가 만료가 되었는지, 존재하는지 확인
        // 만료가 되었을 때, 다시 재 인증 메일을 보낼 수 있도록.
        RedisAuthKey redisAuthKey = redisAuthKeyRepository.findById(authKey).orElseThrow(
                () -> new SpringRedditException("redis Auth_Key 가 만료됨"));

        fetchUserAndEnable(redisAuthKey.getUserUuid());
    }

    // 이메일 인증을 통해 활성화를 풀어준다.
    public void fetchUserAndEnable(String uuid) {
        // 가져온 uuid의 값으로 user의 활성화 상태 변경
        VerificationToken verificationToken = verificationTokenRepository.findByUuid(uuid).orElseThrow(() ->
                new SpringRedditException("UUID Not Match User - " + uuid));

        User user = verificationToken.getUser();
        user.setEnable(true);

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        user.addAuthority(authority);
        userRepository.save(user);
    }

    // 닉네임 중복 확인
    public boolean checkUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        return (user == null) ? true : false;
    }

    // email 중복 확인
    public boolean checkEmail(String email) {
        User user = userRepository.findOneWithAuthoritiesByEmail(email).orElse(null);
        return (user == null) ? true : false;
    }

    // 로그인
    @Transactional
    public JwtTokenDTO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        // 권한 객체를 생성하려고 authenticate(Token)을 실행할 때, loadUserByUsername 메서드가 실행된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
        // 권한 주입
        SecurityContextHolder.getContext().setAuthentication(authentication);


        // 토큰 생성
        String jwt = jwtProvider.createJws(authentication);
        // 토큰 저장
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
        jwtTokenDTO.setJwtToken(jwt);
//        jwtTokenDTO.setUser(userRepository.findByUsername(authentication.getName())
//                .orElseThrow(() -> new UsernameNotFoundException(authentication.getName() + "을 찾을 수 없습니다.")));
        jwtTokenDTO.setUsername(authentication.getName());
        jwtTokenDTO.setImageUrl(((CustomUserDetails) authentication.getPrincipal()).getImageUrl());

        return jwtTokenDTO;
    }

    public Boolean connectKakao(LoginDTO loginDTO, String kakaoId) {
        User user = userRepository.findByEmail(loginDTO.getEmail()).orElse(null);

        if (user != null) {
            if (passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
                user.setKakaoId(Long.parseLong(kakaoId));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    // 비밀번호 전송
    public User sendToMailFindPw(String email) {
        // emailCheck를 굳이 하지않고 여기서 orElseThrow 를 보내서 Error를 Catch 하는 방법으로 가자.
        User user = userRepository.findOneWithAuthoritiesByEmail(email).orElseThrow(() ->
                new RuntimeException(email + "을 찾을 수 없습니다.")
        );

        String temp = mailService.sendEmailFindPw(user);
        user.setPassword(encodePassword(temp));

        return userRepository.save(user);
    }

    // 카카오 토큰 값 가져오기.
    public JwtTokenDTO getKaKaoToken(String code) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 파라미터 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "acc75518d7960814670dead34e92ba88");
        params.add("redirect_uri", "http://localhost:3000/kakao/login");
        params.add("code", code);
        params.add("client_secret", "kn6fxUCpSbM7kT3yhaGJikHHKkvQQfJb");

        // 헤더와 파라미터를 하나로 뭉침
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        RestTemplate rt = new RestTemplate();

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoTokenDTO kakaoTokenDTO = new KakaoTokenDTO();
        try {
            kakaoTokenDTO = objectMapper.readValue(response.getBody(), KakaoTokenDTO.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return getKaKaoProfile(kakaoTokenDTO);
    }

    // 카카오 Access-Token 으로 사용자 정보 가져오기.
    private JwtTokenDTO getKaKaoProfile(KakaoTokenDTO kakaoTokenDTO) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + kakaoTokenDTO.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        // 토큰을 사용해 사용자 정보 가져옴.
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        // 사용자 정보를 객체에 담음.
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfileDTO profileDTO = null;

        try {
            profileDTO = objectMapper.readValue(response.getBody(), KakaoProfileDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 가져온 사용자의 아이디에 값이 있는지 확인
        User user = userRepository.findByKakaoId(profileDTO.getId()).orElse(null);
        JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();

        System.out.println("user.isEnable() = " + user.isEnable());

        if (!user.isEnable()) { // 이메일 인증이 안되었을 경우
            System.out.println("예외처리 안으로 들어갑니다.");
            throw new UserNotEmailVerificationException("dkseoa");
        }

        // 값이 있고 이메일 인증이 되어있는 지 확인
//        jwtTokenDTO.setJwtToken(jwtProvider.kakaoCreateJws(user.getAuthorities(), user));
        jwtTokenDTO.setUsername(user.getUsername());


        return jwtTokenDTO;
    }
}
