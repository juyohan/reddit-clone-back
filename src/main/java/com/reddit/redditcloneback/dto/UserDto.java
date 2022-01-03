package com.reddit.redditcloneback.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.reddit.redditcloneback.model.Role;
import com.reddit.redditcloneback.model.User;
import com.reddit.redditcloneback.model.enumeration.RoleType;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDto {
    public static class Request {
        @Data
        public static class Login {
            private String email;
            private String password;
        }

        @Data
        @NoArgsConstructor
        public static class Create {
            private String email;
            private String nickname;
            private String password;
        }
    }

    public static class Response {
        @Data
        @AllArgsConstructor
        public static class UserDetail {
            private String nickname;
            private String email;
            private Long kakaoId;
            private String imagePath;
            private Collection<? extends GrantedAuthority> roles;
            private boolean enable;

            public static UserDetail entityToDto(User user) {
                return new UserDetail(
                        user.getNickname(),
                        user.getEmail(),
                        user.getKakaoId(),
                        user.getImagePath(),
                        changeRole(user.getRoles()),
                        user.isEnable()
                );
            }

            private static Collection<? extends GrantedAuthority> changeRole(Set<Role> roles){
                return roles.stream().map(role -> new SimpleGrantedAuthority(role.getType().getName()))
                                .collect(Collectors.toList());
            }
        }

        @Data
        @AllArgsConstructor
        public static class LoginDetail {
            private String nickname;
            private String token;
            private String imagePath;
            private Long tokenExpiredTime;
        }
    }

    public static class QueryDsl {
        @Data
        public static class WithRole {
            private String nickname;
            private String email;
            private String imagePath;
            private Long kakaoId;
            private Collection<? extends GrantedAuthority> roleTypes;
            private boolean enable;

            @QueryProjection
            public WithRole(String nickname, String email, String imagePath, Long kakaoId, Set<RoleType> roleTypes, boolean enable) {
                Collection<? extends GrantedAuthority> collection = changeRole(roleTypes);

                this.nickname = nickname;
                this.email = email;
                this.imagePath = imagePath;
                this.kakaoId = kakaoId;
                this.roleTypes = collection;
                this.enable = enable;
            }

            private static Collection<? extends GrantedAuthority> changeRole(Set<RoleType> roleTypes) {
                return roleTypes.stream().map(roleType -> new SimpleGrantedAuthority(roleType.getName()))
                        .collect(Collectors.toList());
            }
        }

        @Data
        public static class EnableOfUser {
            private boolean enable;

            @QueryProjection
            public EnableOfUser(boolean enable) {
                this.enable = enable;
            }
        }
    }
}
