package com.reddit.redditcloneback.repository.custom;

import com.reddit.redditcloneback.dto.UserDto;

import java.util.Optional;

public interface UserCustomRepository {
    Optional<UserDto.QueryDsl.WithRole> searchWithRoleBy(String email, Long kakaoId);
    Optional<UserDto.QueryDsl.EnableOfUser> searchBy(String email, String nickname);

    void deleteUserByNickname(String nickname);
    void updateVerificationSuccessUser(String nickname);
}
