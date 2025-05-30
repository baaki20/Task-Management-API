package com.cloudnova.taskmanagementapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskManagementOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080/api/v1");
        devServer.setDescription("Development server");

        Server prodServer = new Server();
        prodServer.setUrl("https://api.taskmanagement.com/v1");
        prodServer.setDescription("Production server");

        Contact contact = new Contact();
        contact.setEmail("support@taskmanagement.com");
        contact.setName("Task Management API Team");
        contact.setUrl("https://www.taskmanagement.com");

        License mitLicense = new License()
                .name("MIT License")
                .url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Task Management API")
                .version("1.0.0")
                .contact(contact)
                .description("A comprehensive REST API for managing tasks with full CRUD operations, status tracking, and search capabilities.")
                .termsOfService("https://www.taskmanagement.com/terms")
                .license(mitLicense);

        return new OpenAPI()
                .info(info)
                .servers(List.of(devServer, prodServer));
    }
}