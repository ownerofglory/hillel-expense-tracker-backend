package ua.ithillel.expensetracker.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ua.ithillel.expensetracker.model.security.HillelUserDetails;

import java.util.Date;

@Component
public class JwtUtil {
    private static final Long TOKEN_VALID = 24 * 60 * 60 * 1000L;

    @Value("${jwt.secret}")
    private String secretKey;

    public Claims parseToken(String jwt) {
        JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

        return parser.parseSignedClaims(jwt).getPayload();
    }

    public String generateToken(HillelUserDetails user) {
        return Jwts.builder()
                .id("" + user.getId())
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_VALID))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
