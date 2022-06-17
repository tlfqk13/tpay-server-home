package com.tpay.commons.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 적용은 추후 KTP 웹 API 소개 페이지에서 활용될 것으로 초기 적용만 해둠
 * 버전에따라 deprecated 메서드도 있으므로 문서를 참고하여 살펴보면 좋다.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket restAPI() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.tpay.domains"))
            .paths(PathSelectors.any())
            .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("KTP REST API")
            .version("1.0.0")
            .description("KTP SERVER REST API DOCS")
            .build();
    }
}
