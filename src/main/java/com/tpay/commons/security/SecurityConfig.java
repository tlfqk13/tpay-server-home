package com.tpay.commons.security;

import com.tpay.commons.jwt.JwtAuthenticationFilter;
import com.tpay.commons.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security Configuration 클래스. 현재 모든 요청(/**)에 대하여 permitAll() 한다.(서블릿 필터단에서 검증작업을 수행하지 않는다.)
 */
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    private final TokenProvider provider;
    @Bean
    @Profile(value = "security")
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        frameOptions(http);
        sessionManagement(http);
        csrfOption(http);
        authorizedRequest(http);
        addJwtFilter(http);
        return http.build();
    }

    @Bean
    @Profile(value = "security")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration conf = new CorsConfiguration();
        conf.setAllowedOrigins(List.of("*"));
        conf.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", conf);
        return source;
    }

    private void frameOptions(HttpSecurity http) throws Exception {
        http
                .headers()
                .frameOptions()
                .disable();
    }

    private void sessionManagement(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    private void csrfOption(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable();
    }

    private void authorizedRequest(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(StringUtils.toStringArray(getPermitAllList())).permitAll()
                .anyRequest().authenticated();
    }

    private List<String> getPermitAllList() {
        List<String> permitAllList = new ArrayList<>(
                List.of(
                        "/sign-in",
                        "/sign-up",
                        "/sign-out",
                        "/refresh",
                        "/categories",
                        "/certifications/**",
                        "/franchisee/password/exists/**",
                        "/franchisee/password/selfCertification",
                        "/franchisee/password/out",
                        "/admin/**",
                        "/validate/**",
                        "/points/batch",
                        "/external/**",
                        "/push/batch/**",
                        "/refund/limit",
                        "/error",
                        "/h2-console/**",
                        "/favicon.ico",
                        "/push/**",
                        "/notice/**",

                        "/sign-in-new"
                        ));

        if (!profileName.equals("deploy")) {
            permitAllList.addAll(List.of("/swagger-resources/**",
                    "/swagger-ui.html/**",
                    "/swagger-ui/**",
                    "/webjars/**",
                    "/v2/**",
                    "/fcm/**"));
        }

        return permitAllList;
    }

    private void addJwtFilter(HttpSecurity http) {
        http
                .addFilterBefore(new JwtAuthenticationFilter(provider), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
