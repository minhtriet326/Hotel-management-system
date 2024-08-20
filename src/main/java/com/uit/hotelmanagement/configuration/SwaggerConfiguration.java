package com.uit.hotelmanagement.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("API Hotel Management app endpoints")
                .version("1.0")
                .description("This is Hotel Management app endpoints");
        return new OpenAPI()
                .info(info)
//                .addSecurityItem(new SecurityRequirement().addList("JWT Bearer"))
//                .components(new Components()
//                        .addSecuritySchemes("Bearer Token",
//                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
//                                        .scheme("bearer").bearerFormat("JWT")));
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .addSecurityItem(new SecurityRequirement().addList("JWT Bearer"))

                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Token",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer"))

                        .addSecuritySchemes("JWT Bearer",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
