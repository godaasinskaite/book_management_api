package com.app.book_management.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPIs configuration class for setting up API documentation.
 * Allows easy exploration and testing of this book management API.
 *
 *  * <a href="http://localhost:8080/swagger-ui.html">Swagger UI</a>.
 */
@Configuration
public class SwaggerConfig {

    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Book Management API")
                        .version("1.0")
                        .description("Documentation for the Book Management API."));
    }
}