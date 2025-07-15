package br.com.priscyladepaula.desafioitau.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private String apiKeyValue;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Controle Financeiro API")
                        .version("1.0")
                        .description("API para gerenciamento financeiro"))
                .addSecurityItem(new SecurityRequirement().addList("api-key"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("api-key", new SecurityScheme()
                                .name("api-key")
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)));
    }
}
