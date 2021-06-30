package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.*;
import com.reddit.redditcloneback.DTO.JwtTokenDTO;
import com.reddit.redditcloneback.DTO.LoginDTO;
import com.reddit.redditcloneback.DTO.UserDTO;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.RedisDAO.RedisAuthKey;
import com.reddit.redditcloneback.Repository.*;
import com.reddit.redditcloneback.Util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.time.LocalDateTime.now;

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
    public String signUp(UserDTO userDTO) {
        if (userRepository.findOneWithAuthoritiesByEmail(userDTO.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 존재한 유저입니다.");
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encodePassword(userDTO.getPassword()));
//        user.setCreateDate(now());
        user.setEnable(false);

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
        RedisAuthKey redisAuthKey = redisAuthKeyRepository.findById(authKey).orElseThrow(
                () -> new SpringRedditException("redisAuthkey가 만료됨"));

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

    // 로그인
    public JwtTokenDTO login(LoginDTO loginDTO) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

            // 권한 있는지 확인
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);
            // 권한 주입
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 토큰 생성
            String jwt = jwtProvider.createJws(authentication);
            // 토큰 저장
            JwtTokenDTO jwtTokenDTO = new JwtTokenDTO();
            jwtTokenDTO.setJwtToken(jwt);
            jwtTokenDTO.setUsername(authentication.getName());

            return jwtTokenDTO;
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findByUsername(username);
    }

}
