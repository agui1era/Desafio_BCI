package bci.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Deshabilita la protección CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Sesiones sin estado
                .and()
                .authorizeRequests() // Configura las reglas de autorización
                .antMatchers("/api/users/sign-up").permitAll() // Permite acceso a /api/users/sign-up
                .antMatchers("/api/users/login").permitAll()   // Permite acceso a /api/users/login
                .anyRequest().authenticated(); // Requiere autenticación para cualquier otra petición
    }
}