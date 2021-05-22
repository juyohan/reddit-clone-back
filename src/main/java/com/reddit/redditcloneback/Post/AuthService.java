package com.reddit.redditcloneback.Post;

import com.reddit.redditcloneback.DAO.*;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.Repository.UserRepository;
import com.reddit.redditcloneback.Repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.reddit.redditcloneback.Config.Constants.ACTIVATION_EMAIL;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;

    public void signUp(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encodePassword(registerRequest.getPassword()));
        user.setCreateDate(now());
        user.setEnable(false);


        userRepository.save(user);

        String token = generateVerificationToken(user);
        System.out.println(token);

        mailService.sendMail(user, token);
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString(); // 네트워크 상에서 고유성이 보장되는 id를 만들기 위한 표준 규약
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setUser(user);
        verificationToken.setToken(token);

        verificationTokenRepository.save(verificationToken);

        return token;
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void verifyAccount(String token, String username, String authKey) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        verificationTokenOptional.orElseThrow(() -> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    public void fetchUserAndEnable(VerificationToken verificationToken) {
        String authKey = verificationToken.getUser().getAuthKey();
        User user = userRepository.findByAuthKey(authKey).orElseThrow(() ->
                new SpringRedditException("User Not Found with id - " + verificationToken.getUser().getUsername()));
        user.setEnable(true);
        userRepository.save(user);
    }

    public int checkUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            return 1;
        else
            return 0;
    }
}
