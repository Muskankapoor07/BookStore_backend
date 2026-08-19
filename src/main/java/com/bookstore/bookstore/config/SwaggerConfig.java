package com.bookstore.bookstore.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi bookstoreApi() {
        return GroupedOpenApi.builder()
                .group("bookstore")
                .pathsToMatch("/bookstore_user/**")
                .build();
    }
}







//package com.bookstore.bookstore.config;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.info.Info;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI swaggerOpenAPI() {
//
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Bookstore App Swagger")
//                        .version("1.0")
//                        .description("API documentation for Bookstore Application"));
//    }
//}