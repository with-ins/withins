package com.withins.api.config.vue;

import lombok.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

@Configuration
public class VueStaticConfig implements WebMvcConfigurer {


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(getResolver());
    }

    private PathResourceResolver getResolver() {
        return new PathResourceResolver() {

            @Override
            protected Resource getResource(@NonNull String resourcePath, @NonNull Resource location) throws IOException {
                Resource requestedResource = location.createRelative(resourcePath);
                if (requestedResource.exists() && requestedResource.isReadable()) {
                    return requestedResource;
                } else {
                    return new ClassPathResource("/static/index.html");
                }
            }
        };
    }

}
