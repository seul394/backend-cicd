package cloudclub.blog.users.jwt;

import cloudclub.blog.users.repository.UserRepository;
import cloudclub.blog.users.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final UserRepository userRepository;

    public JwtTokenProvider(@Value("${jwt.secret}") String key, UserRepository userRepository) {
        byte[] secretBytes = Base64.getDecoder().decode(key);
        this.key = Keys.hmacShaKeyFor(secretBytes);
        this.userRepository = userRepository;
    }

    public JwtToken generateToken(Authentication authentication) {
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*30*36))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(getEmail(token), null, null);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    public String getEmail(String token) {
        try {
            Optional<User> user = userRepository.findByEmail(
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token)
                            .getBody()
                            .getSubject()
            );
            if (!user.isPresent()) {
                throw new IllegalArgumentException("사용자가 없습니다.");
            }
            return user.get().getEmail();
        } catch (ExpiredJwtException e) {
            throw new IllegalStateException("Expired JWT Token");
        }
    }
}
