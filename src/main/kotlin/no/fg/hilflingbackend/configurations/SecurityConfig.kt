package no.fg.hilflingbackend.configurations

import no.fg.hilflingbackend.repository.PhotoGangBangerRepository
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

@Configuration
@EnableWebSecurity
class SecurityConfig(
  private val photoGangBangerRepository: PhotoGangBangerRepository,
) {
  private val swaggerWhitelist =
    arrayOf(
      "/",
      "/swagger-ui/**",
      "/swagger-ui.html",
      "/v3/api-docs/**",
      "/error",
      "/.well-known/jwks.json",
    )

  @Bean
  fun securityFilterChain(
    http: HttpSecurity,
    jwtAuthFilter: JwtAuthFilter,
  ): SecurityFilterChain {
    http
      .csrf { csrf ->
        // CSRF protection is disabled (stateless api). Maybe we need it idk
        csrf.ignoringRequestMatchers("/**")
      }.headers { headers -> headers.frameOptions { it.sameOrigin() } }
      .sessionManagement {
        it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      }.authorizeHttpRequests { auth ->
        auth
          .requestMatchers(*swaggerWhitelist)
          .permitAll()
          .requestMatchers(HttpMethod.POST, "/auth/login")
          .permitAll()
          .requestMatchers(HttpMethod.OPTIONS, "/**")
          .permitAll()
          // read endpoints that are intentionally public
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
          .requestMatchers(
            HttpMethod.GET,
            "/photo_gang_bangers/actives",
          ).permitAll()
          .requestMatchers(
            HttpMethod.GET,
            "/photo_gang_bangers/active_pangs",
          ).permitAll()
          .requestMatchers(
            HttpMethod.GET,
            "/photo_gang_bangers/inactive_pangs",
          ).permitAll()
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
      }.addFilterBefore(
        jwtAuthFilter,
        UsernamePasswordAuthenticationFilter::class.java,
      )

    return http.build()
  }

  @Bean
  fun userDetailsService(): UserDetailsService =
    UserDetailsService { username ->
      // we are pre-authenticated here
      val user =
        photoGangBangerRepository.findByUsername(username)
          ?: throw UsernameNotFoundException("User '$username' was not found")

      val authorities =
        listOf(
          SimpleGrantedAuthority("ROLE_USER"),
          SimpleGrantedAuthority("ROLE_ALLE"),
        )

      // password is not used for JWT-authenticated requests.
      User
        .withUsername(user.username)
        .password("{noop}jwt-auth-only")
        .authorities(authorities)
        .build()
    }

  @Bean
  fun authenticationManager(
    configuration: AuthenticationConfiguration,
  ): AuthenticationManager = configuration.authenticationManager
}
