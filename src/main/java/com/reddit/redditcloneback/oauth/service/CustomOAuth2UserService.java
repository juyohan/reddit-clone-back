package com.reddit.redditcloneback.oauth.service;

import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.oauth.dto.CustomOAuth2User;
import com.reddit.redditcloneback.oauth.dto.OAuthAttributes;
import com.reddit.redditcloneback.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService
        implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 서비스를 구분 (kakao, naver, google 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // oauth2 로그인 시 키의 이름 (kakao = id)
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, usernameAttributeName, oAuth2User.getAttributes());

        // 사용자가 카카오 로그인이 연동이 되어있는지 확인한다.
        UserDto.QueryDsl.WithRole userDetail = userRepository.searchWithRoleBy("",oAuthAttributes.getId()).orElseThrow(
                () -> new BusinessException(Exceptions.USER_NOT_FOUND)
        );

        return new CustomOAuth2User(
                oAuthAttributes.getAttributes(),
                userDetail.getRoleTypes(),
                oAuthAttributes.getName(),
                userDetail.getImagePath(),
                userDetail.getEmail(),
                userDetail.getNickname()
        );
    }
}
