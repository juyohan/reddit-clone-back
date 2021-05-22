package com.reddit.redditcloneback.Post;

import com.reddit.redditcloneback.DAO.NotificationEmail;
import com.reddit.redditcloneback.DAO.TempKey;
import com.reddit.redditcloneback.DAO.User;
import com.reddit.redditcloneback.Error.SpringRedditException;
import com.reddit.redditcloneback.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;

    @Async
    void sendMail(User user, String token) {
        String authKey = new TempKey().getKey(50, false);

        user.setAuthKey(authKey);
        userRepository.flush();

        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Please Activate Your Account!");
            messageHelper.setText(new StringBuffer().append("<h1>\"이메일 인증\"</h1><br><br>")
                    .append("<h3>" + user.getUsername() + "님</h3><br>")
                    .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                    .append("<a href='http://localhost:8080/api/auth/accountVerification?uid=")
                    .append(token)
                    .append("&username=")
                    .append(user.getUsername())
                    .append("&authKey=")
                    // 인증키를 생성해야함.
                    .append(authKey)
                    .append("' target='_blenk'>이메일 인증 확인</a>")
                    .toString(), true);
            messageHelper.setFrom("BookRed", "BookRed Manager");
        };
        try {
            mailSender.send(message);
        } catch (MailException e) {
            System.out.println("실패했다.");
            System.out.println(e);
        }

    }
}
