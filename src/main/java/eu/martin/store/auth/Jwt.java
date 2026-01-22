package eu.martin.store.auth;

import eu.martin.store.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

class Jwt {
    private final Claims claims;
    private final SecretKey secretKey;

    Jwt(Claims claims, SecretKey secretKey) {
        this.claims = claims;
        this.secretKey = secretKey;
    }

    boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }

    Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    @Override
    public String toString() {
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }
}
