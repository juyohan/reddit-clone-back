package com.reddit.redditcloneback.oauth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private Long id;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, Long id) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if (registrationId.equals("kakao"))
            return ofKakao(userNameAttributeName, attributes);
        return new OAuthAttributes();
    }

    // 카카오 토크으로 사용자 정보를 가져왔을 때, 해당 값들에 접근을 하기위함.
    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");
        Long id = Long.parseLong(String.valueOf(attributes.get("id")));

        return new OAuthAttributes(attributes,
                userNameAttributeName,
                String.valueOf(profile.get("nickname")),
                String.valueOf(kakao_account.get("email")),
                id
        );
    }
}
