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
                "/refund/receipt/**",
                "/refund/admin-cancel/**",
                "/error",
                "/h2-console/**",
                "/favicon.ico",
                "/push/**",
                // TODO: 2022/07/26 test url 권한 추가
                "/test/admin/**",
                "/test/refunds/**",
                "/test/points/**",
                "/notice/**",
                "/duplicate",
                // TODO: 2022/09/26 NICE_VAN 관련 데이터 로직
                "/order/**",
                // TODO: 2022/09/15 tourCash 환급 전용
                "/refund/approval/tourcash/**",
                // NICE VAN 관련
                "/van/**",
                "/franchisee/admin/cms/downloads",
                "/franchisee/admin/vat/downloads",
                "/customer/**",
                "/refund/after/**", // URI Parsing 추가하기 애매, uri 방식을 바꾸는게 향후 이득
                "/api/**"
        ));

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
