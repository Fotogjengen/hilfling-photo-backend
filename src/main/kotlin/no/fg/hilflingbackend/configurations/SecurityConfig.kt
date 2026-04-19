package no.fg.hilflingbackend.configurations

import no.fg.hilflingbackend.repository.SamfundetUserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig(
        private val samfundetUserRepository: SamfundetUserRepository,
) {

  // CORS
  @Value(
          "\${cors.allowed-origins:http://localhost:3000,http://localhost:5173,http://localhost:8888}"
  )
  private lateinit var allowedOrigins: String

  // Auth CORS.
  // Auth endpoints should only EVER be called from the proxy, so we must have a stricter policy
  @Value("\${auth.cors.allowed-origins:http://localhost:8888}")
  private lateinit var authAllowedOrigins: String

  private val swaggerWhitelist =
          arrayOf(
                  "/",
                  "/swagger-ui/**",
                  "/swagger-ui.html",
                  "/v3/api-docs/**",
                  "/error",
          )

  @Bean
  fun securityFilterChain(
          http: HttpSecurity,
          jwtAuthFilter: JwtAuthFilter,
  ): SecurityFilterChain {
    http
            .csrf { it.disable() }
            .cors { it.configurationSource(corsConfigurationSource()) }
            .headers { headers -> headers.frameOptions { it.sameOrigin() } }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
              auth.requestMatchers(*swaggerWhitelist)
                      .permitAll()
                      .requestMatchers(HttpMethod.POST, "/auth/login")
                      .permitAll()
                      .requestMatchers(HttpMethod.OPTIONS, "/**")
                      .permitAll()

                      // Read endpoints that are intentionally public
                      .requestMatchers(HttpMethod.GET, "/photos/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/eventcards/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/motives/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/search/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/searchSuggestions/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/photo_gang_bangers/actives")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/photo_gang_bangers/active_pangs")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/photo_gang_bangers/inactive_pangs")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/photo_gang_bangers/*")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/albums/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/categories/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/positions/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/places/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/event_owners/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/security_levels/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/phototags/**")
                      .permitAll()
                      .requestMatchers(HttpMethod.GET, "/gangs/**")
                      .permitAll()

                      // TODO: DETTE MÅ FJERNES!!!
                      .requestMatchers("/seed", "/seed/**")
                      .permitAll()
                      .anyRequest()
                      .authenticated()
            }
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  fun userDetailsService(): UserDetailsService = UserDetailsService { username ->
    val user =
            samfundetUserRepository.findByUsername(username)
                    ?: throw UsernameNotFoundException("User '$username' was not found")

    val securityLevel = user.securityLevel.securityLevelType?.name ?: "ALLE"
    val authorities =
            listOf(
                    SimpleGrantedAuthority("ROLE_USER"),
                    SimpleGrantedAuthority("ROLE_$securityLevel"),
            )

    // Password is not used for JWT-authenticated requests.
    User.withUsername(user.username)
            .password("{noop}jwt-auth-only")
            .authorities(authorities)
            .build()
  }

  @Bean
  fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager =
          configuration.authenticationManager

  @Bean
  fun corsConfigurationSource(): CorsConfigurationSource {
    val source = UrlBasedCorsConfigurationSource()

    // Auth endpoints should only EVER be called from the proxy, so we must have a stricter policy
    source.registerCorsConfiguration("/auth/**", createCorsConfig(authAllowedOrigins))
    source.registerCorsConfiguration("/**", createCorsConfig(allowedOrigins))
    return source
  }

  private fun createCorsConfig(origins: String): CorsConfiguration {
    val config = CorsConfiguration()
    config.allowCredentials = true
    config.allowedOrigins = origins.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    config.allowedMethods = listOf("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
    config.allowedHeaders = listOf("Authorization", "Content-Type", "X-hilfling-token")
    config.exposedHeaders = listOf("X-hilfling-token")
    return config
  }
}
