package my.code.nftmarketplacepublic.security;

import com.jdbc.nftmarketplace2.filters.CustomAuthorizationFilter;
import com.jdbc.nftmarketplace2.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserRepo userRepo;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/login/**", "/api/token/refresh/**", "/api/auth",
                        "/api", "/api/registrations/**", "/api/logout/**").permitAll()
                .requestMatchers(
                        "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                        "/swagger-resources/**", "/webjars/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/users/getResetCode").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/confirmCode").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users/dropForgottenPassword").permitAll()
                .requestMatchers("/api/nfts/forSale", "/api/users/rateOfUserByDays").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/nfts/viewNftById/{id}").permitAll()
                .requestMatchers("/api/users/registration").permitAll()
                .requestMatchers("/api/users/getProfile/{id}").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/users").hasAnyRole("USER")
                .requestMatchers(HttpMethod.DELETE,"/api/nftCollections").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/roles/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .oauth2Login(oauth2Login ->
                        oauth2Login
                                .defaultSuccessUrl("/api/auth/google", true)
                )
                .apply(CustomSecurityDetails.customDsl(userRepo))
                .and()
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout().logoutUrl("/api/logout").logoutSuccessUrl("/api/login").invalidateHttpSession(true);
        return http.build();
    }

    @Bean
    CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:5173/", "http://localhost:8080/",
                "https://nft-market-place-f-23-c6a5ee8f518d.herokuapp.com/", "https://magic-nft-marketplace.netlify.app/",
                "https://nft-marketplace-sigma-topaz.vercel.app/"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Authorization", "Origin, Accept", "X-Requested-With", "Access-Control-Request-Method",
                "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}