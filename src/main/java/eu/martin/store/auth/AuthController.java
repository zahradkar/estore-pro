package eu.martin.store.auth;

import eu.martin.store.users.UserResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("${app.auth-path}")
@RestController
class AuthController {
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @Value("${app.auth-path}")
    private String authPath;

    @PostMapping("/login")
    JwtResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) { // test passed
        var loginResult = authService.login(request);

        var refreshToken = loginResult.refreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath(authPath + "/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.accessToken().toString());
    }

    @PostMapping("/refresh")
    JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) { // test passed (even if user is not logged in)
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

    @GetMapping("/me")
    ResponseEntity<UserResponse> me() { // test passed
        return ResponseEntity.ok(authService.getCurrentUserInfo());
    }
}
