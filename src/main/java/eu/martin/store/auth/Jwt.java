package eu.martin.store.auth;

import eu.martin.store.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public record Jwt(Claims claims, SecretKey secretKey) {
    private static final String TOKEN_ID = "tokenId";

    boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    @Override
    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }

    boolean notAccess() {
        return !claims().get(TOKEN_ID).equals(TokenId.ACCESS.name());
    }

    boolean notRefresh() {
        return !claims().get(TOKEN_ID).equals(TokenId.REFRESH.name());
    }

    public boolean isVerification() {
        return claims().get(TOKEN_ID).equals(TokenId.VERIFICATION.name());
    }
}
