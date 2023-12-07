package com.example.round.auth;

import com.example.round.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final RedisUtil redisUtil;

    /**
     * 111111~999999 사이의 6자리 인증 번호 생성
     * @param email : 인증 받을 대학교 이메일
     */
    @Transactional
    public void sendCode(String email){
        SecureRandom random = new SecureRandom();
        String authKey = String.valueOf(random.nextInt(888888) + 111111);
        sendAuthEmail(email, authKey);
    }
    
    /**
     * 인증 번호 HTML 파일 이메일 전송
     * @param email : 인증 받을 대학교 이메일
     * @param authKey : 인증 번호
     */
    private void sendAuthEmail(String email, String authKey){
        String subject = "Round 대학교 이메일 인증번호를 확인하세요.";
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "utf-8");
            message.setTo(email); //인자로 받은 이메일 수신자 주소
            message.setSubject(subject); //제목

            //템플릿에 전달할 데이터 설정
            Context context = new Context();
            context.setVariable("verificationCode", authKey); //Template에 전달할 데이터(authKey) 설정
            //메일 내용 설정 : 템플릿 프로세스
            String html = springTemplateEngine.process("mail/index", context);
            message.setText(html, true);
//            message.addInline("image", new ClassPathResource("templates/images/image-1.jpeg"));

            javaMailSender.send(mimeMessage);
            log.info("success");
        } catch(MessagingException e) {
            e.printStackTrace();
        }

        //Redis에 3분동안 인증코드 {email, authKey} 저장
        try {
            redisUtil.setDataExpire(email, authKey, 60 * 3L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
