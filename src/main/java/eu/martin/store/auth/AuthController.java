package eu.martin.store.auth;

import eu.martin.store.users.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("${app.auth-path}")
@RestController
class AuthController {
    private final JwtConfig jwtConfig;
    private final AuthService authService;

    @Value("${app.auth-path}")
    private String authPath;

    /*
     * todo add
     * Account lockouts
     * Brute-force protection
     */

    @Operation(summary = "Provides login using e-mail and password.")
    @PostMapping("/login")
    JwtResponse loginViaCredentials(@Valid @RequestBody LoginRequest request, HttpServletResponse response) { // test passed
        var loginResult = authService.loginViaCredentials(request);

        return getJwtResponse(response, loginResult);
    }

    private JwtResponse getJwtResponse(HttpServletResponse response, LoginResponse loginResult) {
        var refreshToken = loginResult.refreshToken().toString();
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath(authPath + "/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponse(loginResult.accessToken().toString());
    }

    @Operation(summary = "Provides one-time login using a link in sent e-mail.")
    @GetMapping("/login-via-email/{token}")
    JwtResponse loginViaEmail(@PathVariable String token, HttpServletResponse response) {
        log.debug("Processing loginViaEmail request.");
        log.info("loginViaEmail - endpoint in development");

        var loginResult = authService.loginViaEmail(token);

        return getJwtResponse(response, loginResult);
    }

    @Operation(summary = "Sends an e-mail with a link for one-time login.")
    @GetMapping("{email}")
    void sendEmailForLogin(@PathVariable String email) {
        authService.sendEmailForLogin(email);
    }

    @Operation(summary = "Returns refresh token.")
    @PostMapping("/refresh")
    JwtResponse refresh(@CookieValue(value = "refreshToken") String refreshToken) { // test passed (even if user is not logged in)
        var accessToken = authService.refreshAccessToken(refreshToken);
        return new JwtResponse(accessToken.toString());
    }

    @Operation(summary = "Returns currently logged in user.")
    @GetMapping("/me")
    ResponseEntity<UserResponse> me() { // test passed
        return ResponseEntity.ok(authService.getCurrentUserInfo());
    }
}
