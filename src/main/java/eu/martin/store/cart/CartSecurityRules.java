package eu.martin.store.cart;

import eu.martin.store.auth.SecurityRules;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

@Component
class CartSecurityRules implements SecurityRules {
    @Value("${app.cart-path}")
    private String cartPath;

    @Override
    public void configure(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
        registry.requestMatchers(cartPath + "/**").permitAll();
    }
}
