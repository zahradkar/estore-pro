package eu.martin.store.users;

import eu.martin.store.common.JwtConfig;
import eu.martin.store.common.JwtResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
record AuthController(JwtConfig jwtConfig, UserMapper userMapper, AuthService authService) {
    @PostMapping("/login")
    JwtResponse login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        var loginResult = authService.login(request);

        var refreshToken = loginResult.refreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.accessToken().toString());
    }

    @PostMapping("/refresh")
    JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) {
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

    @GetMapping("/me")
    ResponseEntity<UserResponse> me() {
        var user = authService.getCurrentUser();
        if (user == null)
            return ResponseEntity.notFound().build();

        var userResponse = userMapper.toResponse(user);
        return ResponseEntity.ok(userResponse);
    }
}
