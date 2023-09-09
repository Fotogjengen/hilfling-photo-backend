package no.fg.hilflingbackend.configurations;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class ResourceConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
         * Configures where to get resources from
         * // TODO better description
         * */
        registry.addResourceHandler("/photos/**")
                .addResourceLocations("file:photos/");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
        .addMapping("/**")
        .allowedOrigins("http://localhost:3000")
        .allowedOrigins("http://localhost:8000")
        .allowedOrigins("http://localhost:8080")
        .allowedMethods("GET", "PATCH", "POST", "DELETE", "OPTIONS")
        .allowedHeaders("*");
    }

}
