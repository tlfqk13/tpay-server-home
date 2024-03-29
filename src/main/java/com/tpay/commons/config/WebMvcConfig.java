package com.tpay.commons.config;

import com.tpay.commons.interceptor.JwtValidationInterceptor;
import com.tpay.commons.interceptor.PrintRequestInterceptor;
import com.tpay.commons.util.resolver.KtpIndexInfoResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final PrintRequestInterceptor printRequestInterceptor;
    private final JwtValidationInterceptor jwtValidationInterceptor;
    private final KtpIndexInfoResolver indexInfoResolver;
    @Value("${spring.config.activate.on-profile}")
    private String profileName;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exclusivePathList = new ArrayList<>(List.of("/sign-in",
                "/sign-up",
                "/duplicate-sign-out",
                "/refresh",
                "/categories",
                "/certifications/**",
                "/franchisee/password/exists/**",
                "/franchisee/password/selfCertification/**",
                "/franchisee/password/out/**",
                "/admin/**",
                "/validate/**",
                "/refund/receipt",
                "/refund/admin-cancel/**",
                "/notice/**",
                "/duplicate",
                "/order/**",
                "/refunds/confirm", // refund 서버에서 사후환급 상태 업데이트

                // 외부 연동 컨트롤러 (외부에서 접속하는 controller)
                "/admin/**",        // ERP
                "/push/**",         // FCM, ADMIN 과 연동
                "/external/**",     // 그로잉세일즈
                "/van/**",          // NICE VAN
                "/tourcash/**",
                "/franchisee/admin/cms/downloads",
                "/franchisee/admin/vat/downloads",
                "/franchisee/admin/hometax/immediate/downloads",
                "/franchisee/admin/hometax/after/downloads",
                "/franchisee/admin/vat/quarterly/downloads", // 분기별 다운 - 관리자 버전
                "/customer/**",
                "/api/**",          // KTP-API

                // 테스트 관련 컨트롤러
                "/test/**",
                "/refund/receipt/**",

                // h2 및 기타 설정파일
                "/error",
                "/h2-console/**",
                "/favicon.ico",

                // dev 전용
                "/dev/**"
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
        registry.addInterceptor(printRequestInterceptor).addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH");
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(indexInfoResolver);
    }
}
