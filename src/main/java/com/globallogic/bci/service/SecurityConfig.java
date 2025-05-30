package main.java.com.globallogic.bci.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Define el bean para el codificador de contraseñas.
     * Se utilizará BCrypt para encriptar y verificar las contraseñas. [cite: 16]
     * @return una instancia de BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .cors() // Habilita la configuración CORS definida en WebConfig
            .and()
            .csrf().disable() // Deshabilita CSRF ya que se usa JWT (stateless)
            .authorizeRequests()
                .antMatchers("/sign-up").permitAll() // Permite el acceso al endpoint de creación de usuarios [cite: 10]
                .antMatchers("/login").permitAll() // Permite el acceso al endpoint de login (deseable)
                .antMatchers("/h2-console/**").permitAll() // Permite el acceso a la consola H2 [cite: 15]
                .anyRequest().authenticated() // Todas las demás solicitudes requieren autenticación
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Configura la gestión de sesiones como stateless debido al uso de JWT [cite: 14]

        // Necesario para permitir que la consola H2 se muestre en un frame
        http.headers().frameOptions().sameOrigin();
    }
}