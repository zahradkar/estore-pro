package eu.martin.store.admin;

import eu.martin.store.auth.SecurityRules;
import eu.martin.store.users.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
class AdminSecurityRules implements SecurityRules {
    @Value("${app.admin-path}")
    private String adminPath;

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(adminPath + "/**").hasRole(Role.ADMIN.name());
    }
}
