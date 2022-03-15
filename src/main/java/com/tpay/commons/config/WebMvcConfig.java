package com.tpay.commons.config;

import com.tpay.commons.interceptor.AuthInterceptor;
import com.tpay.commons.interceptor.PrintRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
  private final AuthInterceptor authInterceptor;
  private final PrintRequestInterceptor printRequestInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(authInterceptor)
        .addPathPatterns("/**")
        .excludePathPatterns(
            "/sign-in",
            "/sign-up",
            "/sign-out",
            "/refresh",
            "/categories",
            "/certifications/**",
            "/franchisee/business-number",
            "/franchisee/password",
            "/admin/**",
            "/refunds",
            "/h2-console/**",
            "/favicon.ico",
            "/validate/**",
            "/refund/approval/fake",
            "/franchisee/password/**",
            "/points/batch",
            "/error");
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
