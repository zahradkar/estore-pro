package eu.martin.store.users;

import eu.martin.store.auth.SecurityRules;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
class UserSecurityRules implements SecurityRules {

    @Value("${app.user-path}")
    private String userPath;

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {

        registry.requestMatchers(HttpMethod.POST, userPath).permitAll()
                .requestMatchers(HttpMethod.GET, userPath).hasRole(Role.ADMIN.name()) // for get all users
                .requestMatchers(HttpMethod.DELETE, userPath + "/**").authenticated()
                .requestMatchers(HttpMethod.PUT, userPath + "/**").authenticated()
                .requestMatchers(HttpMethod.POST, userPath + "/*/change-password").authenticated()
                .requestMatchers(HttpMethod.GET, userPath + "/verify/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/email-verification*").permitAll();
    }
}
