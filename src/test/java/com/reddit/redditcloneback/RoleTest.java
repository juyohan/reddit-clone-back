package com.reddit.redditcloneback;

import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class RoleTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void initialData() {
        User user = User.builder()
                .enable(true)
                .imagePath("")
                .password("asdf")
                .email("dygks8557@naver.com")
                .nickname("jupaka")
                .build();

        userRepository.save(user);
    }

    @Test
    public void roleTest() {

    }
}
