package bci.api.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("SecurityConfig Unit Tests")
public class SecurityConfigUnitTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("Should load application context successfully")
    void contextLoads() {
        assertThat(applicationContext).isNotNull();
    }

    @Test
    @DisplayName("Should create BCryptPasswordEncoder bean")
    void passwordEncoderBeanShouldBeCreated() {
        assertThat(applicationContext.getBean(BCryptPasswordEncoder.class)).isNotNull();
    }
}