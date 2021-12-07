package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.User.Authority;
import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.DTO.OAuthAttributes;
import com.reddit.redditcloneback.DTO.UserDTO.CustomOAuth2User;
import com.reddit.redditcloneback.JWT.JwtProvider;
import com.reddit.redditcloneback.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 서비스를 구분 (kakao, naver, google 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // oauth2 로그인 시 키의 이름 (kakao = id)
        String usernameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, usernameAttributeName, oAuth2User.getAttributes());

        User user = userRepository.findByKakaoId(attributes.getId()).orElseThrow(
                () -> new UsernameNotFoundException(""+attributes.getId())
        );

        Collection<SimpleGrantedAuthority> collection = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

//        return CustomOAuth2User.create(user, attributes.getAttributes());
        return new CustomOAuth2User(user.getUsername(), collection, user.getImageUrl(), attributes.getAttributes());
    }
}
