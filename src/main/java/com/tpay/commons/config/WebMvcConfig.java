package com.tpay.commons.config;

import com.tpay.commons.interceptor.AuthInterceptor;
import com.tpay.commons.interceptor.JwtValidationInterceptor;
import com.tpay.commons.interceptor.PrintRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthInterceptor authInterceptor;
    private final PrintRequestInterceptor printRequestInterceptor;
    private final JwtValidationInterceptor jwtValidationInterceptor;

    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exclusivePathList = new ArrayList<>(List.of("/sign-in",
            "/sign-up",
            "/sign-out",
            "/duplicate-sign-out",
            // TODO: 2022/09/07 계정 삭제 건 추가
            "/delete-account",
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
            // TODO: 2022/07/26 test url 권한 추가
            "/test/admin/**",
            "/test/refunds/**",
            "/test/points/**",
            "/notice/**",
            "/duplicate"));

        if (!profileName.equals("deploy")) {
            exclusivePathList.addAll(List.of("/swagger-resources/**",
                "/swagger-ui.html/**",
                "/swagger-ui/**",
                "/webjars/**",
                "/v2/**",
                "/fcm/**"));
        }

        registry
            .addInterceptor(jwtValidationInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(exclusivePathList);
        registry
            .addInterceptor(authInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(exclusivePathList);
        registry.addInterceptor(printRequestInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
    }
}
