package com.app.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "API 문서", description = "API에 대한 설명문서", version = "1.0",
    contact = @Contact(name = "hiddentrap", email = "hiddentrap@gmail.com")))
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi testOpenApi() {
    String[] paths = {"/api/**"};
    return GroupedOpenApi.builder()
                         .group("TEST API v1")
                         .pathsToMatch(paths)
                         .addOpenApiCustomizer(securityOpenApi())
                         .build();
  }

  private OpenApiCustomizer securityOpenApi() {
    return OpenApi -> OpenApi.addSecurityItem(new SecurityRequirement().addList("jwt token"))
                             .getComponents()
                             .addSecuritySchemes("jwt token", new SecurityScheme()
                                 .name("Authorization")
                                 .type(Type.HTTP)
                                 .in(In.HEADER)
                                 .bearerFormat("JWT")
                                 .scheme("Bearer"));
  }
}
