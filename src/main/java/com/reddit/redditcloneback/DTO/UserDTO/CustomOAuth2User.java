package com.reddit.redditcloneback.DTO.UserDTO;

import com.reddit.redditcloneback.DAO.User.Authority;
import com.reddit.redditcloneback.DAO.User.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class CustomOAuth2User implements OAuth2User
//        , UserDetails
{
    private String name;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;
    private String imageUrl;
//    private String password;
//    private String username;

//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }

    public CustomOAuth2User(String name, Collection<? extends GrantedAuthority> authorities, String imageUrl
                            ,Map<String, Object> attributes
//            , String email, String password
    ) {
        this.name = name;
        this.authorities = authorities;
        this.imageUrl = imageUrl;
        this.attributes = attributes;
//        this.username = email;
//        this.password = password;
    }

//    public static CustomOAuth2User create (User user) {
//        Collection<SimpleGrantedAuthority> collection = user.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
//                .collect(Collectors.toList());
//        return new CustomOAuth2User(
//                user.getUsername(),
//                collection,
//                user.getImageUrl(),
//                user.getEmail(),
//                user.getPassword()
//        );
//    }

//    public static CustomOAuth2User create (User user, Map<String, Object> attributes) {
//        CustomOAuth2User customOAuth2User = CustomOAuth2User.create(user);
//        customOAuth2User.setAttributes(attributes);
//        return customOAuth2User;
//    }
}
