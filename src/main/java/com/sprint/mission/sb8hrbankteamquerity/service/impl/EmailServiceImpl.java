package com.sprint.mission.sb8hrbankteamquerity.service.impl;

import com.sprint.mission.sb8hrbankteamquerity.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    //송신자
    private static final String ADMIN_EMAIL = "superjj0520@gmail.com";

    //기존 백업 서비스를 지연시키지 않기 위해 비동기 처리
    @Async
    public void sendBackupStatusEmail(Long backupId, boolean isSuccess, String errorMessage) {
        String status = isSuccess ? "성공 (COMPLETED)" : "실패 (FAILED)";
        String subject = String.format("[Backup Log] 백업 작업 %s - ID: %d", status, backupId);

        StringBuilder text = new StringBuilder();
        text.append("<h3>백업 작업 리포트</h3>");
        text.append("<p><strong>작업 ID:</strong> ").append(backupId).append("</p>");
        text.append("<p><strong>결과:</strong> ").append(status).append("</p>");

        if (!isSuccess) {
            text.append("<p style='color:red;'><strong>에러 원인:</strong></p> ").append(errorMessage).append("</p>");
        } else {
            text.append("<p>데이터가 안전하게 백업되었습니다.</p>");
        }
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 송신자
            helper.setFrom(fromEmail);
            // 발신자
            helper.setTo(ADMIN_EMAIL);
            helper.setSubject(subject);
            helper.setText(text.toString(), true);

            javaMailSender.send(message);
            log.info("백업 상태 알람 메일 발송 완료(ID = {})", backupId);
        } catch (MessagingException e) {
            log.error("백업 상태 알람 메일 발송 실패", e);
        }
    }
}
