package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.model.Role;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.model.enumeration.RoleType;
import com.reddit.redditcloneback.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * 권한을 부여하는 함수
     * @param user 권한을 부여할 사용자의 Entity
     */
    public void empowerOfUser(User user) {
        roleRepository.save(
                Role.builder()
                        .user(user)
                        .type(RoleType.USER)
                        .build()
        );
    }
}
