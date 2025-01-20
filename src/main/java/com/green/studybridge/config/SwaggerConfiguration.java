package com.green.studybridge.config;

import com.green.studybridge.config.constant.InfoConst;
import com.green.studybridge.config.constant.JwtConst;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@OpenAPIDefinition(
//    info = @Info(
//          title = "JWT"
//        , description = "JWT Practice"
//        , version = "v0.1"
//    )
//    , security = @SecurityRequirement(name = "Authorization")
//)
//
//@SecurityScheme(
//          type = SecuritySchemeType.HTTP
//        , name = "Authorization"
//        , in = SecuritySchemeIn.HEADER
//        , bearerFormat = "JWT"
//        , scheme = "Bearer"
//)
@Configuration
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final InfoConst infoConst;
    private final JwtConst jwtConst;

    @Bean
    public OpenAPI openAPI() {
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtConst.getHeaderKey());

        SecurityScheme securityScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP)
                                                    .in(SecurityScheme.In.HEADER)
                                                    .name(jwtConst.getHeaderKey())
                                                    .scheme(jwtConst.getScheme())
                                                    .bearerFormat(jwtConst.getBearerFormat());

        return new OpenAPI().components(
                            new Components().addSecuritySchemes(jwtConst.getHeaderKey(), securityScheme))
                                            .addSecurityItem(securityRequirement)
                                            .info(new Info().title(infoConst.getTitle())
                                            .description(infoConst.getDescription())
                                            .version(infoConst.getVersion())
               );
    }


}
