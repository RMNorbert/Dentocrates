package com.rmnnorbert.dentocrates.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {
    @Value("${dentocrates.dev-url}")
    private String developmentUrl;
    private final String BASE_URL = System.getenv("BASE_URL");
    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(developmentUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("dentocrates@dentocrates.dentocrates");
        contact.setName("Dentocrates");
        contact.setUrl(BASE_URL);

        License unLicense = new License().name("Unlicense").url("https://choosealicense.com/licenses/unlicense/");

        Info info = new Info()
                .title("Dentocrates Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage Dentocrates. It supports JSON format and requires API key authentication.")
                .termsOfService("https://github.com/RMNorbert/Dentocrates")
                .license(unLicense);

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(info).servers(List.of(devServer));
    }
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
