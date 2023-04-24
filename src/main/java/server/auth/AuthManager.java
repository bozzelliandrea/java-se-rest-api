package server.auth;

import database.MemoryDataLayer;
import database.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public final class AuthManager {

    public final static String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";
    public final static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
            SignatureAlgorithm.HS256.getJcaName());

    public static Optional<String> register(AuthRequest.Register request) {

        if (!request.isValid())
            return Optional.empty();

        User user = new User(MemoryDataLayer.userSequence.incrementAndGet(),
                request.getUsername(),
                request.getEmail(),
                request.getPassword());

        MemoryDataLayer.user.put(user.getId(), user);

        return Optional.of(generateToken(request.getUsername()));
    }

    public static String login(AuthRequest.Login request) {

        return "";
    }

    public static String login(String token) {
        return "";

    }

    public static String refresh(String token) {
        return "";

    }

    private static String generateToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claim("username", username)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(5L, ChronoUnit.MINUTES)))
                .signWith(hmacKey)
                .compact();
    }
}
