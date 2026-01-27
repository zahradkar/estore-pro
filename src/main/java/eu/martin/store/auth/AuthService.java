package eu.martin.store.auth;

import eu.martin.store.common.Utils;
import eu.martin.store.email.EmailService;
import eu.martin.store.users.User;
import eu.martin.store.users.UserMapper;
import eu.martin.store.users.UserRepository;
import eu.martin.store.users.UserResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper mapper;
    private final EmailService emailService;

    @Value("${app.base-uri}")
    private String baseUri;

    @Value("${app.auth-path}")
    private String authPath;

    UserResponse getCurrentUserInfo() {
        return mapper.toUserResponse(getCurrentUser());
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(Utils.USER_NOT_LOGGED_IN));
    }

    LoginResponse loginViaCredentials(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        return getLoginResponse(request.email());
    }

    private LoginResponse getLoginResponse(String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired())
            throw new BadCredentialsException("Invalid refresh token");

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return jwtService.generateAccessToken(user);
    }

    void sendEmailForLogin(String email) {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException(Utils.USER_NOT_FOUND));

        if (user.isVerified()) {
            var jwt = jwtService.generateVerificationToken(user);
            var path = baseUri + authPath + "/login-via-email/" + jwt.toString();
            emailService.sendEmail(
                    email,
                    "E-shop: one time login",
                    """
                            Dear %s,<br>
                            <a href=%s>enter</a> to our E-shop."""
                            .formatted(user.getName(), path)
            );
            log.debug("E-mail for logging in was sent to {}", email);
        } else
            log.warn("E-mail for logging in was NOT sent due to unverified {} address.", email);
    }

    LoginResponse loginViaEmail(String token) {
        var jwt = jwtService.parseToken(token);
        if (jwt == null || jwt.isExpired() || !jwt.isVerification())
            throw new BadCredentialsException("Login via e-mail failed: invalid token");

        return getLoginResponse(jwt.getEmail());
    }
}
