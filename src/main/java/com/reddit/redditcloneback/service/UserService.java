package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.UserRepository;
import com.reddit.redditcloneback.security.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.reddit.redditcloneback.exception.Exceptions.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    /**
     * 로그인이 되어있는 상태에서 사용자의 닉네임을 가져오는 함수이다.
     * @return 만약, 인증 정보에 주요한 정보가 있다면, 닉네임을 반환한다.
     */
    public String getCurrentUserNickname() {
        CustomUserDetails principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal.equals("anonymousUser"))
            throw new BusinessException(Exceptions.USER_NOT_LOGIN);

        return principal.getUsername();
    }

    /**
     * 로그인이 되어있는 상태에서 사용자의 Entity 를 가져오는 함수이다.
     * @return 닉네임으로 사용자 Entity 를 찾아 반환한다.
     */
    public User getCurrentUser() {
        String nickname = getCurrentUserNickname();
        return findOneByNickname(nickname);
    }

    /**
     * 이메일과 닉네임의 중복 확인을 수행하는 함수이다.
     * @param email 중복 확인 할 이메일의 데이터, 필수적이 아니다.
     * @param nickname 중복 확인 할 닉네임의 데이터, 필수적이 아니다.
     */
    public void checkByEmailOrNickname(String email, String nickname) {
        if (userRepository.searchBy(email, nickname).orElse(null) != null)
            throw new BusinessException(USER_EXISTED);
    }

    /**
     * 혹시모를 대비를 위해 저장하기 전, 다시 한번 중복확인을 한다.
     * 그리고 DB 에 사용자의 정보를 저장하는 함수이다.
     * @param create email, password, nickname 의 정보가 담긴 객체이다.
     * @return 저장한 사용자 Entity 를 DTO 로 변환을 한 뒤, 반환한다.
     */
    public UserDto.Response.UserDetail createUser(UserDto.Request.Create create) {
        checkByEmailOrNickname(create.getEmail(), "");
        checkByEmailOrNickname("", create.getNickname());

        User user = userRepository.save(
                User.builder()
                        .email(create.getEmail())
                        .password(passwordEncoder.encode(create.getPassword()))
                        .nickname(create.getNickname())
                        .imagePath("http://localhost:8080/api/file/user/image?fileName=init.png")
                        .build()
        );

        return UserDto.Response.UserDetail.entityToDto(user);
    }

    /**
     * 이메일 인증이 성공적으로 완료된 사용자에게 활성화 상태와 권한을 부여하는 함수이다.
     * @param nickname 권한을 부여받을 사용자의 닉네임 데이터이다.
     * @return 사용자의 활성화 상태를 반환한다.
     */
    @Transactional
    public boolean successSignup(String nickname) {
        User user = findOneByNickname(nickname);
        user.setEnable(true);

        roleService.empowerOfUser(user);

        return user.isEnable();
    }

    /**
     * DB 에서 사용자를 찾는 함수이다.
     * @param nickname 사용자를 찾기 위해서 사용하는 닉네임 데이터이다.
     * @return 파라미터로 받은 닉네임으로 되어있는 정보가 있다면, 사용자의 Entity 를 반환한다.
     */
    public User findOneByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(
                () -> new BusinessException(USER_NOT_FOUND)
        );
    }
}
