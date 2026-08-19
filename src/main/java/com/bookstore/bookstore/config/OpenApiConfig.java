package com.bookstore.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Bookstore App Swagger")
                        .version("1.0")
                        .description("API documentation for Bookstore Application"))

                .components(new Components()

                        // Admin API Key
                        .addSecuritySchemes(
                                "admin_api_key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("x-access-token")
                                        .description(
                                                "authorised token after ADMIN_USER login"
                                        )
                        )

                        // User API Key
                        .addSecuritySchemes(
                                "user_api_key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("x-access-token")
                                        .description(
                                                "authorised token after USER login"
                                        )
                        )
                );
    }
}