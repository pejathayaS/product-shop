package com.philips.productservicems.config;

import java.util.Arrays;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The swagger configuration picks up all the apis defined within the basePackage of the consuming service
 *     title: Product Service
 *     Description: Exposes API for CRUD operations on Product
 *     contactEmail:shobith.pejathaya@philips.com
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket applicationWorkFlowApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select().apis(RequestHandlerSelectors.basePackage("com.philips"))
                .paths(PathSelectors.any())
                .build().enable(true)
                .apiInfo(getApiInfo());

    }

    private ApiInfo getApiInfo() {
        return new ApiInfo("Product Shop",
                "Retain service offering product operations",
                "1.0", null,
                null,
                null, null, Collections.emptyList());
    }
}