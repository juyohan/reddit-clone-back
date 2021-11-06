package com.reddit.redditcloneback.Service;

import com.reddit.redditcloneback.Key.TempKey;
import com.reddit.redditcloneback.DAO.User.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;
    private final RedisService redisService;

    @Async
    String sendMail(User user, String uidToken) {
//        user.setAuthKey(authKey);
        String authKey = redisService.setAuthKeyToRedis(uidToken);
//        userRepository.flush();

        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Please Activate Your Account!");
            messageHelper.setText(new StringBuffer().append("<h1>\"이메일 인증\"</h1><br><br>")
                    .append("<h3>" + user.getUsername() + "님</h3><br>")
                    .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                    .append("<a href='http://localhost:8080/api/auth/accountVerification?userEmail=")
                    .append(user.getEmail())
                    .append("&authKey=")
                    // 인증키를 생성해야함.
                    .append(authKey)
                    .append("' target='_blenk'>이메일 인증 확인</a>")
                    .toString(), true);
            messageHelper.setFrom("BookRed", "BookRed Manager");
        };
        try {
            mailSender.send(message);
            return authKey;
        } catch (MailException e) {
            System.out.println(e);
            return null;
        }
    }

    // 임시 비밀번호 보내기
    @Async
    String sendEmailFindPw(User user) {
        String tempPw = new TempKey().getKey(8, true);

        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("BookRed 임시 비밀번호");
            messageHelper.setText(new StringBuffer()
                    .append("<h1>\"임시 비밀번호 생성\"</h1><br><br>")
                    .append("<h3>" + user.getUsername() + "님</h3><br>")
                    .append("<p>임시 비밀번호는 아래와 같습니다.</p><br><br>")
                    .append("<h2>\"" + tempPw + "\"</h2><br>")
                    .append("<p>로그인 후엔 비밀번호를 변경해주세요.</p>")
                    .toString(), true);
            messageHelper.setFrom("BookRed", "BookRed Manager");
        };
        try {
            mailSender.send(message);
            return tempPw;
        } catch (MailException e) {
            System.out.println("e = " + e);
            return null;
        }
    }
}
