package com.thadsayan.heracles.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("Heracles API")
                        .version("1.0.0")
                        .description("""
                                Heracles is a multi-user gym tracking REST API.
                                """))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local development")
                ))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .tags(List.of(
                        new Tag()
                                .name("Authentication")
                                .description("User registration, login and token refresh"),
                        new Tag()
                                .name("Exercises")
                                .description("Public and custom exercises"),
                        new Tag()
                                .name("Workouts")
                                .description("Workout creation and workout history"),
                        new Tag()
                                .name("User Exercises")
                                .description("User-specific exercise queries"),
                        new Tag()
                                .name("User Workouts")
                                .description("User-specific workout queries")
                ));
                openAPI.addExtension("x-tagGroups", List.of(
                        Map.of(
                                "name", "Core API",
                                "tags", List.of("Authentication", "Exercises", "Workouts")
                        ),
                        Map.of(
                                "name", "User Queries",
                                "tags", List.of("User Exercises", "User Workouts")
                        )
                ));
        return openAPI;
    }
}