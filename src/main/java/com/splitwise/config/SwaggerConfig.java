package com.splitwise.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Splitwise API")
                        .version("1.0.0")
                        .description("API documentation for Splitwise expense splitting application"))
                .tags(List.of(
                    new Tag().name("users").description("User management operations"),
                    new Tag().name("groups").description("Group management operations"),
                    new Tag().name("expenses").description("Expense tracking operations")
                ));
    }
}
