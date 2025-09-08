package com.sapient.productengineering.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig{
    @Bean
    public OpenAPI usersOpenAPI(){
        return new OpenAPI().info(new Info()
                .title("Users API")
                .version("1.0")
                .description("Users management APIs"));
    }
}
