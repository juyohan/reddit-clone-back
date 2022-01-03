package com.reddit.redditcloneback;

import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.model.Role;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.model.enumeration.RoleType;
import com.reddit.redditcloneback.repository.RoleRepository;
import com.reddit.redditcloneback.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class LoginTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    /**
     * One To Many 에 대한 데이터 가져오기.
     */
    @Test
    public void test() {
        User user1 = User.builder()
                .kakaoId(0L)
                .enable(true)
                .imagePath("")
                .password("asdf")
                .email("dygks8557@naver.com")
                .nickname("jupaka")
                .build();

        userRepository.save(user1);

        Role role = Role.builder()
                .type(RoleType.USER)
                .build();
        role.addRole(user1);

        roleRepository.save(role);

        Role role1 = Role.builder()
                .type(RoleType.ADMIN)
                .build();
        role1.addRole(user1);

        roleRepository.save(role1);

        String email = "dygks8557@naver.com";
        UserDto.QueryDsl.WithRole userAndRole = userRepository.searchWithRoleBy(email, null).orElseThrow(
                () -> new BusinessException(Exceptions.USER_NOT_FOUND)
        );
        System.out.println("userAndRole = " + userAndRole);
    }
}
