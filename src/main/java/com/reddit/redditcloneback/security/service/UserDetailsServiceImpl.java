package com.reddit.redditcloneback.security.service;

import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.UserRepository;
import com.reddit.redditcloneback.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * UsernamePasswordAuthenticationToken 이 생성이 되었을 때, 자동으로 실행이 되는 함수이다.
     * 로그인을 통해 가져온 이메일의 정보가 DB 에 존재한다면, createUser 함수를 실행하고 반환한다.
     * @param email 로그인을 요청한 이메일의 데이터
     * @return 로그인을 요청한 사용자의 정보를 권한이 담긴 알맞는 형태로 반환한다.
     */
    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmailWithRoles(email)
                .map(user -> createUser(user))
                .orElseThrow(
                        () -> new BusinessException(Exceptions.USER_NOT_FOUND)
                );
    }

    /**
     * 로그인을 하기 위해서 여러가지 정보들을 담아내는 함수이다.
     * 로그인 요청을 한 사용자의 활성화 상태가 제대로 되어있다면, 권한을 따로 추출하고 알맞은 형태로 반환한다.
     * @param user 로그인 요청을 한 사용자의 Entity
     * @return Filter 에서 필요한 형태의 객체를 반환
     */
    private CustomUserDetails createUser(User user) {
        // 비활성화가 되어있으면 예외를 던지고 멈춘다.
        if (!user.isEnable()) {
            throw new BusinessException(Exceptions.USER_DISABLE);
        }

        List<GrantedAuthority> userRoles = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getType().getName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getNickname(), user.getPassword(), userRoles, user.getImagePath());
    }
}
