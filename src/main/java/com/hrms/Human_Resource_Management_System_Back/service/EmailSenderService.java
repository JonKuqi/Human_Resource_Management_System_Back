package com.hrms.Human_Resource_Management_System_Back.service;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service class for sending emails asynchronously.
 * <p>
 * This service is responsible for sending verification emails to users. It uses a thread pool to send emails
 * in parallel without blocking the main application flow, improving the performance of the application.
 * </p>
 */
public class EmailSenderService {

    /**
     * The mail sender used to send emails.
     */
    private static JavaMailSender mailSender;

    /**
     * A thread pool for sending emails asynchronously. It allows up to 5 concurrent threads for email sending.
     */
    private static ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     * Initializes the {@link JavaMailSender} to be used for sending emails.
     * <p>
     * This method is typically called on application startup (e.g., in a {@link @PostConstruct} method) to set up
     * the mail sender once, preventing the need for repeated initialization.
     * </p>
     *
     * @param sender the {@link JavaMailSender} instance to be used for sending emails
     */
    public static void initialize(JavaMailSender sender) {
        mailSender = sender;
    }

    /**
     * Sends a verification email asynchronously.
     * <p>
     * This method submits the email sending task to the thread pool for asynchronous execution. It creates
     * a {@link SimpleMailMessage}, sets the necessary fields (recipient, subject, body, and sender), and then
     * sends the email using the {@link JavaMailSender}.
     * </p>
     *
     * @param to      the recipient email address
     * @param subject the subject of the email
     * @param body    the body/content of the email
     */
    public static void sendVerificationEmail(String to, String subject, String body) {
        executor.submit(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("kucijon@gmail.com");  // Sender's email address
                mailSender.send(message);
                System.out.println("Verification email sent to: " + to);
            } catch (Exception e) {
                System.err.println("Failed to send email to: " + to);
                e.printStackTrace();
            }
        });
    }


    /**
     * Shuts down the email sender's executor service.
     * <p>
     * This method gracefully shuts down the executor service, stopping any pending email sending tasks
     * and releasing resources.
     * </p>
     */

    public static void sendApplicationStatusEmail(String to, String applicantName, String jobTitle, String newStatus) {
        String subject = "Application Status Update";
        String body = String.format(
                "Dear %s,\n\nYour application for the position \"%s\" has been updated.\n" +
                        "Current Status: %s\n\nThank you for applying!\n\nBest regards,\nHR Team",
                applicantName, jobTitle, newStatus
        );

        sendVerificationEmail(to, subject, body);
    }
    /**
     * Sends a simple email asynchronously.
     * <p>
     * This method submits the email sending task to the thread pool for asynchronous execution. It creates
     * a {@link SimpleMailMessage}, sets the necessary fields (recipient, subject, body, and sender), and then
     * sends the email using the {@link JavaMailSender}.
     * </p>
     *
     * @param to      the recipient email address
     * @param subject the subject of the email
     * @param body    the body/content of the email
     */

    public static void sendSimpleEmail(String to, String subject, String body) {
        executor.submit(() -> {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(to);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("kucijon@gmail.com");
                mailSender.send(message);
                System.out.println("Email sent to: " + to);
            } catch (Exception e) {
                System.err.println("Failed to send email to: " + to);
                e.printStackTrace();
            }
        });
    }
    /**
     * Shuts down the email sender's executor service.
     * <p>
     * This method gracefully shuts down the executor service, stopping any pending email sending tasks
     * and releasing resources.
     * </p>
     */

    public static void shutdown() {
        executor.shutdown();
    }


}