package com.hrms.Human_Resource_Management_System_Back.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordHasherTest {

    @Test
    void generateSalt_shouldReturnBase64EncodedString() {
        // Act
        String salt = PasswordHasher.generateSalt();

        // Assert
        assertNotNull(salt);
        assertFalse(salt.isEmpty());
    }

    @Test
    void generateSaltedHash_shouldReturnConsistentHashForSameInput() {
        // Arrange
        String password = "password123";
        String salt = PasswordHasher.generateSalt();

        // Act
        String hash1 = PasswordHasher.generateSaltedHash(password, salt);
        String hash2 = PasswordHasher.generateSaltedHash(password, salt);

        // Assert
        assertEquals(hash1, hash2);
    }

    @Test
    void compareSaltedHash_shouldReturnTrueForMatchingPassword() {
        // Arrange
        String password = "password123";
        String salt = PasswordHasher.generateSalt();
        String saltedHash = PasswordHasher.generateSaltedHash(password, salt);

        // Act
        boolean result = PasswordHasher.compareSaltedHash(password, salt, saltedHash);

        // Assert
        assertTrue(result);
    }

    @Test
    void compareSaltedHash_shouldReturnFalseForNonMatchingPassword() {
        // Arrange
        String password = "password123";
        String salt = PasswordHasher.generateSalt();
        String saltedHash = PasswordHasher.generateSaltedHash(password, salt);

        // Act
        boolean result = PasswordHasher.compareSaltedHash("wrongPassword", salt, saltedHash);

        // Assert
        assertFalse(result);
    }
}
