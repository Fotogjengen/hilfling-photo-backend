package no.fg.hilflingbackend.configurations

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
  override fun addCorsMappings(registry: CorsRegistry) {
    val allowedOrigins = System.getenv("ALLOWED_ORIGINS")
      ?.split(",")
      ?.map { it.trim() }
      ?.filter { it.isNotBlank() }
      ?.toTypedArray()
      ?: emptyArray()

    registry.addMapping("/**")
      .allowedOrigins(*allowedOrigins)
      .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
      .allowedHeaders("*")
      .maxAge(3600)
  }
}
