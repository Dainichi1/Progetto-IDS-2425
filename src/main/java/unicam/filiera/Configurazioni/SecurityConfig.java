package unicam.filiera.Configurazioni;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Proteggi la dashboard e altre risorse sensibili
                        .requestMatchers("/dashboard/**").authenticated()
                        .anyRequest().permitAll()
                )
                // Configurazione di logout
                .logout(logout -> logout
                        .logoutUrl("/api/login/logout") // Endpoint per il logout
                        .invalidateHttpSession(true)   // Invalida la sessione
                        .deleteCookies("JSESSIONID")  // Cancella i cookie
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                        })
                )
                .csrf().disable() // Disabilita CSRF se necessario
                .headers(headers -> headers
                        .cacheControl().disable()    // Disabilita il caching
                        .frameOptions().disable()    // Disabilita restrizioni iframe
                );

        return http.build();
    }
}
