package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.DAO.User.User;
import com.reddit.redditcloneback.DTO.CustomUserDetails;
import com.reddit.redditcloneback.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        UserDetails userDetails = userRepository.findOneWithAuthoritiesByEmail(username)
//                .map(user -> createUser(username, user))
//                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없음"));

//        System.out.println("userDetails = " + userDetails);

//        Optional<User> userOptional = userRepository.findByUsername(username);
//        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("No user Found with username " + username));
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.isEnable(), true, true, true, getAuthorities("USER"));
        return userRepository.findOneWithAuthoritiesByEmail(username)
                .map(user -> createUser(username, user))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없음"));
    }

    private CustomUserDetails createUser(String username, User user) {
        // 비활성화가 되어있으면 예외를 던지고 멈춘다.
        if (user.isEnable() == false) {
            throw new RuntimeException(username + "활성화가 되어있지 않음.");
        }

        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getUsername(), user.getPassword(), grantedAuthorities, user.getImageUrl());
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
