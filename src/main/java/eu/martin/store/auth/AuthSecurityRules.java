package eu.martin.store.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
public class AuthSecurityRules implements SecurityRules {
    @Value("${app.auth-path}")
    private String authPath;

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(HttpMethod.POST, authPath + "/login", authPath + "/refresh").permitAll()
                .requestMatchers(HttpMethod.GET, authPath + "/login-via-email/*", authPath + "/*").permitAll();
    }
}
