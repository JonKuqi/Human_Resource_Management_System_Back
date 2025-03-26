package com.hrms.Human_Resource_Management_System_Back.service;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification.
 * <p>
 * This class provides methods to generate a cryptographic salt, hash a password with a salt,
 * and verify a hashed password.
 * </p>
 */
public class PasswordHasher {
    private static final int SALT_LENGTH = 32; // Length of salt in bytes
    private static final int HASH_LENGTH = 256; // Length of hash in bytes
    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     * Generates a cryptographic salt.
     * @return a Base64-encoded string representation of the generated salt
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Generates a salted hash of a given password.
     * @param password the plaintext password to hash
     * @param salt     the cryptographic salt to use
     * @return a hexadecimal string representing the salted hash
     */
    public static String generateSaltedHash(String password, String salt) {
        byte[] hash = hashWithSalt(password, salt);

        // Combine salt and hash into a single string
        StringBuilder sb = new StringBuilder(SALT_LENGTH + HASH_LENGTH);

        byte[] saltBytes = salt.getBytes();
        for (byte saltByte : saltBytes) {
            sb.append(String.format("%02x", saltByte));
        }
        for (byte hashByte : hash) {
            sb.append(String.format("%02x", hashByte));
        }
        return sb.toString();
    }

    /**
     * Compares a given password with a stored salted hash.
     * @param password   the plaintext password to verify
     * @param salt       the cryptographic salt used for hashing
     * @param saltedHash the previously generated salted hash
     * @return {@code true} if the provided password matches the salted hash, {@code false} otherwise
     */
    public static boolean compareSaltedHash(String password, String salt, String saltedHash) {
        String generatedPasswordHash = generateSaltedHash(password, salt);
        return generatedPasswordHash.equals(saltedHash);
    }

    /**
     * Hashes a password using the specified salt and a secure hashing algorithm.
     * @param password the plaintext password to hash
     * @param salt     the cryptographic salt to use
     * @return a byte array containing the hashed password
     */
    private static byte[] hashWithSalt(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.reset();
            digest.update(salt.getBytes());
            byte[] hash = digest.digest(password.getBytes());
            for (int i = 0; i < 1000; i++) {
                digest.reset();
                hash = digest.digest(hash);
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password: " + e.getMessage(), e);
        }
    }
}