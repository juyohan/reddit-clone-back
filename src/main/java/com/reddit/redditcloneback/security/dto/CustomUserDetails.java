package com.reddit.redditcloneback.security.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails, Serializable {

    private static final long serialVersionUID = 174726374856727L;

    private String username;
    private String password;
    private String imagePath;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, List<GrantedAuthority> grantedAuthorities, String imagePath) {
        this.username = username;
        this.password = password;
        this.imagePath = imagePath;
        this.authorities = grantedAuthorities;
        this.isEnabled = true;
        this.isAccountNonExpired = true;
        this.isCredentialsNonExpired = true;
        this.isAccountNonLocked = true;
    }
}
