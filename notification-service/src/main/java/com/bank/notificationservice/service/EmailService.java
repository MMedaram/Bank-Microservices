package com.bank.notificationservice.service;

import com.bank.notificationservice.dto.DownService;
import com.bank.notificationservice.event.AccountCreatedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    public static final String MAIL = "tharapathinaidu.mohan@gmail.com";
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    public void sendEmail(String to, Map<String, Object> templateModel)  {
        try {
            Context context = new Context();
            context.setVariables(templateModel);
            String emailContent = templateEngine.process("transaction-notification-email-template", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(to);
            helper.setSubject("Transaction Notification");
            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
            log.info("Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email");
        }
    }

    public void sendEmailWhenListOfServicesDown(List<DownService> downList)  {
        try {


            Context context = new Context();
            String emailContent = templateEngine.process("service-down", context);

            StringBuilder rows = new StringBuilder();

            for (DownService s : downList) {
                rows.append("<tr>")
                        .append("<td>").append(s.getServiceName()).append("</td>")
                        .append("<td>").append(s.getIpAddress()).append("</td>")
                        .append("</tr>");
            }

            String html = emailContent
                    .replace("{{time}}", LocalDateTime.now().toString())
                    .replace("{{rows}}", rows.toString())
                    .replace("{{eurekaUrl}}", "http://localhost:8080/");

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(MAIL);
            helper.setSubject("List of services down in Bank");
            helper.setText(html, true); // Enable HTML content

            mailSender.send(message);
            log.info("Number of services down , Email sent successfully to {}", MAIL);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email");
        }
    }


    public void sendCustomerOnBordEmail(String to)  {
        try {
            Context context = new Context();
            String emailContent = templateEngine.process("customer-onborded", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(to);
            helper.setSubject("Welcome to MOHAN Bank");
            helper.setText(emailContent, true); // Enable HTML content

            mailSender.send(message);
            log.info("Welcome Email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email");
        }
    }


    public void sendAccountCreationEmail(AccountCreatedEvent event)  {
        try {
            Context context = new Context();
            String emailContent = templateEngine.process("account-created", context);

            String html = emailContent.replace("{{customerName}}", event.getCustomerName())
                    .replace("{{customerNumber}}", event.getCustomerNumber())
                    .replace("{{accountNumber}}", event.getAccountNumber())
                    .replace("{{accountType}}",event.getAccountType())
                    .replace("{{branchName}}",event.getBranchCode())
                    .replace("{{balance}}",String.valueOf(event.getBalance()));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            helper.setTo(event.getCustomerMail());
            helper.setSubject("Account Created");
            helper.setText(html, true); // Enable HTML content

            mailSender.send(message);
            log.info("Account Creation Email sent successfully to {}", event.getCustomerMail());
        } catch (MessagingException e) {
            log.error("Failed to send email", e);
            throw new RuntimeException("Failed to send email");
        }
    }


}
