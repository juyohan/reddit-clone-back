package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.Repository.UserRepository;
import com.reddit.redditcloneback.Util.SecurityUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

//    @Transactional
//    public User userFindWithJwtToken()

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
}
