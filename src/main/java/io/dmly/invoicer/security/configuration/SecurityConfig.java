package io.dmly.invoicer.security.configuration;

import io.dmly.invoicer.security.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

import static io.dmly.invoicer.security.costant.SecurityConstants.PUBLIC_URLS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String[] NOT_SECURED_URLS;

    static {
        NOT_SECURED_URLS = new String[PUBLIC_URLS.size()];
        int i = 0;

        for (String publicUrl : PUBLIC_URLS) {
            NOT_SECURED_URLS[i] = publicUrl + "/**";
            i++;
        }
    }

    private final PasswordEncoder passwordEncoder;
    private final AccessDeniedHandler accessDeniedHandler;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthorizationFilter customAuthorizationFilter;

    @Autowired
    @Qualifier("invoicerUserDetailsService")
    private UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.authorizeHttpRequests(customizer -> customizer.requestMatchers(NOT_SECURED_URLS).permitAll());
        httpSecurity.authorizeHttpRequests(customizer ->
                customizer.requestMatchers(HttpMethod.DELETE, "/api/v1/user/delete/**")
                        .hasAnyAuthority("DELETE:USER"));
        httpSecurity.authorizeHttpRequests(customizer ->
                customizer.requestMatchers(HttpMethod.DELETE, "/api/v1/customer/delete")
                        .hasAnyAuthority("DELETE:CUSTOMER"));
        httpSecurity.exceptionHandling(customizer -> customizer
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationEntryPoint(authenticationEntryPoint));
        httpSecurity.authorizeHttpRequests(customizer -> customizer.anyRequest().authenticated());

        httpSecurity.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

}
