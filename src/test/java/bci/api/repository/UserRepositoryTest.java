package bci.api.repository;

import bci.api.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_whenUserExists_shouldReturnUser() {
        String email = "test@example.com";
        UserEntity user = UserEntity.builder()
                .nombre("Test User")
                .email(email)
                .password("Password123")
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .active(true)
                .token("some-token")
                .phones(Collections.emptyList())
                .build();
        entityManager.persistAndFlush(user);

        Optional<UserEntity> foundUserOptional = userRepository.findByEmail(email);

        assertThat(foundUserOptional).isPresent();
        assertThat(foundUserOptional.get().getEmail()).isEqualTo(email);
        assertThat(foundUserOptional.get().getNombre()).isEqualTo("Test User");
    }

    @Test
    void findByEmail_whenUserDoesNotExist_shouldReturnEmptyOptional() {
        String email = "nonexistent@example.com";

        Optional<UserEntity> foundUserOptional = userRepository.findByEmail(email);

        assertThat(foundUserOptional).isNotPresent();
    }

    @Test
    void findByEmail_whenEmailIsNull_shouldReturnEmptyOptional() {
        Optional<UserEntity> foundUserOptional = userRepository.findByEmail(null);

        assertThat(foundUserOptional).isNotPresent();
    }

    @Test
    void saveUser_shouldPersistUser() {
        String email = "newuser@example.com";
        UserEntity userToSave = UserEntity.builder()
                .nombre("New User")
                .email(email)
                .password("SecurePass!1")
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .active(true)
                .token("new-token")
                .phones(Collections.emptyList())
                .build();

        UserEntity savedUser = userRepository.save(userToSave);
        entityManager.flush();
        entityManager.clear();

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();

        Optional<UserEntity> foundUserOptional = userRepository.findById(savedUser.getId());
        assertThat(foundUserOptional).isPresent();
        assertThat(foundUserOptional.get().getEmail()).isEqualTo(email);
        assertThat(foundUserOptional.get().getNombre()).isEqualTo("New User");
    }
}