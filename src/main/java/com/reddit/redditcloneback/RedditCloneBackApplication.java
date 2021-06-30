package com.reddit.redditcloneback;

import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableCaching
public class RedditCloneBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedditCloneBackApplication.class, args);
    }
}
