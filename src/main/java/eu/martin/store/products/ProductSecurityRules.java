package eu.martin.store.products;

import eu.martin.store.auth.SecurityRules;
import eu.martin.store.users.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
class ProductSecurityRules implements SecurityRules {
    @Value("${app.product-path}")
    private String productPath;

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {

        registry.requestMatchers(HttpMethod.GET, productPath + "/*/logs").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.GET, productPath + "/**").permitAll()
                .requestMatchers(HttpMethod.POST, productPath + "/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.PUT, productPath).hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, productPath + "/**").hasRole(Role.ADMIN.name());
    }
}
