package com.hrms.Human_Resource_Management_System_Back.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSenderService {

    private static JavaMailSender mailSender;

    // Thread pool for sending emails
    private static final ExecutorService executor = Executors.newFixedThreadPool(5);

    // Inject mail sender once on startup (call this from @PostConstruct or config)
    public static void initialize(JavaMailSender sender) {
        mailSender = sender;
    }

    public static void sendVerificationEmail(String to, String subject, String body) {
        executor.submit(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("kucijon@gmail.com");
                mailSender.send(message);
                System.out.println(" Verification email sent to: " + to);
            } catch (Exception e) {
                System.err.println(" Failed to send email to: " + to);
                e.printStackTrace();
            }
        });
    }

    public static void shutdown() {
        executor.shutdown();
    }
}