package com.reddit.redditcloneback.service;

import com.reddit.redditcloneback.common.key.TempKey;
import com.reddit.redditcloneback.dto.UserDto;
import com.reddit.redditcloneback.exception.BusinessException;
import com.reddit.redditcloneback.exception.Exceptions;
import com.reddit.redditcloneback.jwt.JwtFilter;
import com.reddit.redditcloneback.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    /**
     * 이메일 인증을 진행시키기 위해 전송시켜주는 함수이다.
     * @param user 사용자 DTO
     * @param authKey 인증 키의 데한 데이터
     * @return 전송에 성공한다면, 인증 키를 반환한다.
     */
    @Async
    String sendMail(UserDto.Response.UserDetail user, String authKey) {
        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("Please Activate Your Account!");
            messageHelper.setText(new StringBuffer().append("<h1>\"이메일 인증\"</h1><br><br>")
                    .append("<h3>" + user.getNickname() + "님</h3><br>")
                    .append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                    .append("<a href='http://localhost:8081/api/auth/accountVerification?userEmail=")
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
            throw new BusinessException(Exceptions.MAIL_ERROR);
        }
    }

    /**
     *
     * @param user
     * @return
     */
    @Async
    String sendEmailFindPw(User user) {
        String tempPw = new TempKey().getKey(8, true);

        MimeMessagePreparator message = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            messageHelper.setTo(user.getEmail());
            messageHelper.setSubject("BookRed 임시 비밀번호");
            messageHelper.setText(new StringBuffer()
                    .append("<h1>\"임시 비밀번호 생성\"</h1><br><br>")
                    .append("<h3>" + user.getNickname() + "님</h3><br>")
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
            throw new BusinessException(Exceptions.MAIL_ERROR);
        }
    }
}
