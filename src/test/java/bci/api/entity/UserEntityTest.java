package bci.api.entity;

import bci.api.dto.PhoneDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    @Test
    void builder_and_getters_shouldWorkCorrectly() {
        UUID id = UUID.randomUUID();
        String name = "Test User";
        String email = "test@example.com";
        String password = "Password123";
        String token = "jwtToken";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime modified = LocalDateTime.now();
        LocalDateTime lastLogin = LocalDateTime.now();
        boolean isActive = true;

        PhoneEntity phone1 = PhoneEntity.builder()
                .number("123456789")
                .citycode("1")
                .contrycode("57")
                .build();
        List<PhoneEntity> phones = new ArrayList<>();
        phones.add(phone1);

        UserEntity user = UserEntity.builder()
                .id(id)
                .nombre(name)
                .email(email)
                .password(password)
                .token(token)
                .created(created)
                .modified(modified)
                .lastLogin(lastLogin)
                .active(isActive)
                .phones(phones)
                .build();

        assertEquals(id, user.getId());
        assertEquals(name, user.getNombre());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(token, user.getToken());
        assertEquals(created, user.getCreated());
        assertEquals(modified, user.getModified());
        assertEquals(lastLogin, user.getLastLogin());
        assertEquals(isActive, user.isActive());
        assertNotNull(user.getPhones());
        assertEquals(1, user.getPhones().size());
        assertEquals(phone1, user.getPhones().get(0));
    }

    @Test
    void setters_shouldWorkCorrectly() {
        UserEntity user = new UserEntity();

        UUID id = UUID.randomUUID();
        String name = "Another User";
        String email = "another@example.com";
        String password = "NewPassword456";
        String token = "newToken";
        LocalDateTime created = LocalDateTime.now().minusHours(1);
        LocalDateTime modified = LocalDateTime.now();
        LocalDateTime lastLogin = LocalDateTime.now();
        boolean isActive = false;

        PhoneEntity phone2 = new PhoneEntity();
        phone2.setNumber("987654321");
        List<PhoneEntity> phones = new ArrayList<>();
        phones.add(phone2);

        user.setId(id);
        user.setNombre(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setToken(token);
        user.setCreated(created);
        user.setModified(modified);
        user.setLastLogin(lastLogin);
        user.setActive(isActive);
        user.setPhones(phones);

        assertEquals(id, user.getId());
        assertEquals(name, user.getNombre());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(token, user.getToken());
        assertEquals(created, user.getCreated());
        assertEquals(modified, user.getModified());
        assertEquals(lastLogin, user.getLastLogin());
        assertEquals(isActive, user.isActive());
        assertNotNull(user.getPhones());
        assertEquals(1, user.getPhones().size());
        assertEquals(phone2, user.getPhones().get(0));
    }

    @Test
    void noArgsConstructor_shouldInitializeObject() {
        UserEntity user = new UserEntity();
        assertNotNull(user);
    }

    @Test
    void allArgsConstructor_shouldInitializeObject() {
        UUID id = UUID.randomUUID();
        String name = "Full User";
        String email = "full@example.com";
        String password = "Pass7890";
        String token = "fullToken";
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime modified = LocalDateTime.now();
        LocalDateTime lastLogin = LocalDateTime.now();
        boolean isActive = true;
        List<PhoneEntity> phones = new ArrayList<>();
        phones.add(new PhoneEntity());

        UserEntity user = new UserEntity(id, name, email, password, token, created, modified, lastLogin, isActive, phones);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals(name, user.getNombre());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(token, user.getToken());
        assertEquals(created, user.getCreated());
        assertEquals(modified, user.getModified());
        assertEquals(lastLogin, user.getLastLogin());
        assertEquals(isActive, user.isActive());
        assertEquals(phones, user.getPhones());
    }
} 