package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30) //open API 3.0
                .apiInfo(apiInfo())  // 문서 정보
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.waterapi.controller"))  // 스캔할 패키지
                .paths(PathSelectors.any())  // 모든 경로 포함
                .build()
                .useDefaultResponseMessages(true);  // 기본 응답 메시지 활성화 여부. 200, 404
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API Documentation")
                .description("홍수관제소 API 문서")
                .version("1.0.0")
                .build();
    }
}
