package eu.martin.store.auth;

import eu.martin.store.users.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@AllArgsConstructor
@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration(), TokenId.ACCESS.name());
    }

    Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration(), TokenId.REFRESH.name());
    }

    public Jwt generateVerificationToken(User user) {
        return generateToken(user, jwtConfig.getVerificationExpiration(), TokenId.VERIFICATION.name());
    }

    private Jwt generateToken(User user, long tokenExpiration, String tokenId) {
        var claims = Jwts.claims()
                .subject(user.getId().toString())
                .add("email", user.getEmail())
                .add("name", user.getName())
                .add("role", user.getRole())
                .add("tokenId", tokenId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * tokenExpiration))
                .build();

        return new Jwt(claims, jwtConfig.getSecretKey());
    }

    public Jwt parseToken(String token) {
        try {
            return new Jwt(getClaims(token), jwtConfig.getSecretKey());
        } catch (JwtException e) {
            return null;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
