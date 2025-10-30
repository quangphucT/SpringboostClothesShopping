package com.example.demo.service.for_authen;

import com.example.demo.model.for_account.EmailDetails;
import com.example.demo.model.for_account.EmailForgotPasswordDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendOtpMail(EmailDetails emailDetails) {
        try {
            // Truyền dữ liệu vào Thymeleaf template
            Context context = new Context();
            context.setVariable("email", emailDetails.getReceiver().getEmail());
            context.setVariable("otpCode", emailDetails.getOtpCode());
            context.setVariable("expiryTime", emailDetails.getExpiryTime());

            // Render template "otp-template.html"
            String template = templateEngine.process("otp-template", context);

            // Tạo email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("phucp9698@gmail.com");
            helper.setTo(emailDetails.getReceiver().getEmail());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(template, true); // true = gửi dạng HTML

            // Gửi email
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Send OTP email failed!!");
        }
    }

    public void sendLinkResetPasswordPageMail(EmailForgotPasswordDetails emailForgotPasswordDetails) {
        try {
            // Truyền dữ liệu vào Thymeleaf template
            Context context = new Context();
            context.setVariable("email", emailForgotPasswordDetails.getReceiver().getEmail());
            context.setVariable("subject", emailForgotPasswordDetails.getSubject());
            context.setVariable("body", emailForgotPasswordDetails.getBody());

            // Render template "otp-template.html"
            String template = templateEngine.process("email-forgotPassword", context);

            // Tạo email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("phucp9698@gmail.com");
            helper.setTo(emailForgotPasswordDetails.getReceiver().getEmail());
            helper.setSubject(emailForgotPasswordDetails.getSubject());
            helper.setText(template, true); // true = gửi dạng HTML

            // Gửi email
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Send OTP email failed!!");
        }
    }
}
