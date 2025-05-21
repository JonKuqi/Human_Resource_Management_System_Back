package com.hrms.Human_Resource_Management_System_Back.service.tenant;

import com.hrms.Human_Resource_Management_System_Back.service.EmailSenderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> mailMessageCaptor;

    private CountDownLatch emailLatch;

    @BeforeEach
    void setup() throws Exception {
        // Use reflection to reset the static executor field with a test-friendly executor
        Field executorField = EmailSenderService.class.getDeclaredField("executor");
        executorField.setAccessible(true);
        executorField.set(null, java.util.concurrent.Executors.newSingleThreadExecutor());

        // Set up email latch for synchronization
        emailLatch = new CountDownLatch(1);

        // Setup mailSender to count down the latch when send is called
        doAnswer(invocation -> {
            emailLatch.countDown();
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        // Set the mail sender
        setMailSender(mailSender);
    }

    @Test
    void sendApplicationStatusEmail_shouldSendCorrectEmail() throws Exception {
        // Setup - using reflection to inject our mock since EmailSenderService uses static methods
        setMailSender(mailSender);

        // Act
        EmailSenderService.sendApplicationStatusEmail(
                "mirgeta.gashi@student.uni-pr.edu",
                "John Doe",
                "Software Engineer",
                "ACCEPTED"
        );

        // Wait for the email to be sent (with timeout)
        boolean emailSent = emailLatch.await(5, TimeUnit.SECONDS);
        assertTrue(emailSent, "Email sending timed out");

        // Assert
        verify(mailSender).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();

        assertEquals("mirgeta.gashi@student.uni-pr.edu", capturedMessage.getTo()[0]);
        assertEquals("Application Status Update", capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains("John Doe"));
        assertTrue(capturedMessage.getText().contains("Software Engineer"));
        assertTrue(capturedMessage.getText().contains("ACCEPTED"));

        // Clean up
        shutdownExecutor();
    }

    @Test
    void sendSimpleEmail_shouldSendCorrectEmail() throws Exception {
        // Setup
        setMailSender(mailSender);

        // Act
        EmailSenderService.sendSimpleEmail(
                "mirgeta.gashi@student.uni-pr.edu",
                "Test Subject",
                "Test Body"
        );

        // Wait for the email to be sent (with timeout)
        boolean emailSent = emailLatch.await(5, TimeUnit.SECONDS);
        assertTrue(emailSent, "Email sending timed out");

        // Assert
        verify(mailSender).send(mailMessageCaptor.capture());
        SimpleMailMessage capturedMessage = mailMessageCaptor.getValue();
        // Wait for tasks to complete
        Field executorField = EmailSenderService.class.getDeclaredField("executor");
        executorField.setAccessible(true);
        ExecutorService executor = (ExecutorService) executorField.get(null);
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals("mirgeta.gashi@student.uni-pr.edu", capturedMessage.getTo()[0]);
        assertEquals("Test Subject", capturedMessage.getSubject());
        assertEquals("Test Body", capturedMessage.getText());

        // Clean up
        shutdownExecutor();
    }

    // Helper method to use reflection to set the private static field
    private void setMailSender(JavaMailSender mailSender) throws Exception {
        Field field = EmailSenderService.class.getDeclaredField("mailSender");
        field.setAccessible(true);
        field.set(null, mailSender);
    }

    // Helper method to safely shut down the executor
    private void shutdownExecutor() throws Exception {
        Field executorField = EmailSenderService.class.getDeclaredField("executor");
        executorField.setAccessible(true);
        ExecutorService executor = (ExecutorService) executorField.get(null);
        executor.shutdown();
        executor.awaitTermination(2, TimeUnit.SECONDS);
    }
}
