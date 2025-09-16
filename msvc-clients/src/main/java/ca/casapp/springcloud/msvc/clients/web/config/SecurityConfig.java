package ca.casapp.springcloud.msvc.clients.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/authorized").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("USER")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2Login -> oauth2Login
                        .loginPage("/oauth2/authorization/msvc-oidc-client"))
                .oauth2Client(Customizer.withDefaults())
                .build();
    }
}
