package bci.api.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUtil Tests")
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    // A strong, 64-byte Base64 encoded key for general testing
    private String strongSecret = "mD7Ew5y9Q2x8T1V3F6S4K7J0Z9X2C5V8B1N4M7L0P3O6I9U2Y5T8R1E4W7Q0A3S6D9F2G5H8J1K4L7Z0X3C6V9B2N5M8L1P4O7I0U3Y6T9R2E5W8Q1A4S7D0F3G6H9J1K4L7M0";
    // Another strong, 64-byte Base64 encoded key for unsupported token tests
    private String unsupportedSecret = "h8N7O9P2Q5R8S1T4U7V0W3X6Y9Z2A5B8C1D4E7F0G3H6I9J2K5L8M1N4O7P0Q3R6S9T2U5V8W1X4Y7Z0A3B6C9D2E5F8G1H4I7J0K3L6M9N2O5P8Q1R4S7T0U3V6W9X2Y5Z8A1B4C7D0E3F6G9H2I5J8K1L4M7N0";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", strongSecret);
        jwtUtil.init(); // Initialize the key after setting the secret
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String subject = "test@example.com";
        String token = jwtUtil.generateToken(subject);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(subject, jwtUtil.getSubject(token));
    }

    @Test
    void validateToken_shouldReturnTrueForValidToken() {
        String token = jwtUtil.generateToken("test@example.com");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidSignature() {
        String validToken = jwtUtil.generateToken("test@example.com");
        String invalidToken = validToken + "invalid"; // Corrupt the signature
        assertFalse(jwtUtil.validateToken(invalidToken));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        // Manually create a token with a very short expiration (e.g., 100 ms)
        String expiredToken = Jwts.builder()
                .setSubject("expired@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100)) // 100 milliseconds
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(strongSecret)), SignatureAlgorithm.HS512)
                .compact();

        TimeUnit.MILLISECONDS.sleep(200); // Wait for the token to expire

        assertFalse(jwtUtil.validateToken(expiredToken));
    }

    @Test
    void validateToken_shouldReturnFalseForMalformedToken() {
        String malformedToken = "invalid.token.format";
        assertFalse(jwtUtil.validateToken(malformedToken));
    }

    @Test
    void validateToken_shouldReturnFalseForUnsupportedToken() {
        // Create a token signed with a *different* key and algorithm than what JwtUtil expects
        SecretKey differentKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(unsupportedSecret));
        String unsupportedToken = Jwts.builder()
                .setSubject("unsupported@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours
                .signWith(differentKey, SignatureAlgorithm.HS256) // Use a different algorithm and key
                .compact();
        assertFalse(jwtUtil.validateToken(unsupportedToken));
    }

    @Test
    void validateToken_shouldReturnFalseForNullOrEmptyToken() {
        assertFalse(jwtUtil.validateToken(null));
        assertFalse(jwtUtil.validateToken(""));
        assertFalse(jwtUtil.validateToken("   "));
    }

    @Test
    void getSubject_shouldReturnCorrectSubject() {
        String subject = "subject@example.com";
        String token = jwtUtil.generateToken(subject);
        assertEquals(subject, jwtUtil.getSubject(token));
    }

    @Test
    void getExpirationDateFromToken_shouldReturnCorrectDate() {
        String token = jwtUtil.generateToken("test@example.com");
        Date expirationDate = jwtUtil.getExpirationDateFromToken(token);
        assertNotNull(expirationDate);
        assertTrue(expirationDate.after(new Date()));
    }

    @Test
    void getUsernameFromToken_shouldReturnCorrectUsername() {
        String username = "user@example.com";
        String token = jwtUtil.generateToken(username);
        assertEquals(username, jwtUtil.getUsernameFromToken(token));
    }
} 