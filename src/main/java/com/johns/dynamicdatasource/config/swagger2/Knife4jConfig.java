//package com.johns.dynamicdatasource.config.swagger2;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.PathSelectors;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
//
///**
// * knife4j配置
// *
// * @author johns-li
// * @date 2021/06/23
// */
//@Configuration
//@EnableSwagger2
//public class Knife4jConfig {
//
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .enable(true)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.johns"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("DYNAMIC-DATASOURCE-CORE 后台服务API接口文档")
//                .description("DYNAMIC-DATASOURCE-CORE 后台服务API接口文档DYNAMIC-DATASOURCE-CORE 后台服务API接口文档")
//                .termsOfServiceUrl("http://localhost:8080/")
//                .contact("johns-li")
//                .version("1.0.0")
//                .build();
//    }
//}