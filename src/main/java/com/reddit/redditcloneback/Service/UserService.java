package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.Repository.UserRepository;
import com.reddit.redditcloneback.Util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // 현재 로그인이 되어서 SecurityContext에 있는 정보를 토대로 DB에 찔러서 확인함.
    @Transactional
    public User loginAfterFindUserName() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByUsername).orElseThrow(
                () -> new SpringRedditException("찾지 못했숩니다."));

        return user;
    }

    @Transactional
    public User loginAfterFindUserName(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new SpringRedditException(username + "을 찾지 못했습니다."));
        return user;
    }

    // 현재 로그인 되어있는 회원 정보를 가져옴
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 정보가 없을 경우
        if (principal.equals("anonymousUser"))
            return null;
        // 정보가 있을 경우
        else
            return userRepository.findByUsername(((UserDetails) principal).getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("없는 닉네임입니다."));
    }
}
