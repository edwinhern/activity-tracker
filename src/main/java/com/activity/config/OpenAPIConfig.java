package com.activity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenAPIConfig {
  @Bean
  public OpenAPI myOpenAPI() {
    final License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    final Info info = new Info()
        .title("Activity API")
        .version("1.0")
        .description("This API exposes endpoints to manage activity tracker.")
        .license(mitLicense);

    final SecurityRequirement securityRequirement = new SecurityRequirement();
    securityRequirement.addList("Bearer Auth");

    final Components components = new Components()
        .addSecuritySchemes(
            "Bearer Auth",
            new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT"));

    return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components);
  }
}
