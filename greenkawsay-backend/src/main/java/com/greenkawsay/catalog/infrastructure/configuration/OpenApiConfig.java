package com.greenkawsay.catalog.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI Configuration for GreenKawsay Catalog API
 * Provides comprehensive API documentation with security and server information
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "GreenKawsay Catalog API",
        version = "1.0.0",
        description = """
            REST API for managing sustainable products and ecological categories in the GreenKawsay e-commerce platform.
            
            ## Features
            - Product management (CRUD operations)
            - Category management with hierarchical structure
            - Product search and filtering
            - Stock management
            - Pagination support
            
            ## Authentication
            This API uses OAuth2 with Keycloak for authentication. Include the access token in the Authorization header.
            """,
        contact = @Contact(
            name = "GreenKawsay Support",
            email = "support@greenkawsay.com",
            url = "https://greenkawsay.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8081",
            description = "Local Development Server"
        ),
        @Server(
            url = "https://api.greenkawsay.com",
            description = "Production Server"
        )
    },
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT Authentication with Keycloak"
)
public class OpenApiConfig {
}