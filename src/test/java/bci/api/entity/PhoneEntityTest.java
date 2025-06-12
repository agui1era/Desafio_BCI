package bci.api.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PhoneEntity Tests")
class PhoneEntityTest {

    @Test
    void builder_and_getters_shouldWorkCorrectly() {
        UUID id = UUID.randomUUID();
        String number = "123456789";
        String citycode = "1";
        String countrycode = "57";
        UserEntity user = new UserEntity(); // Assuming UserEntity has a no-args constructor

        PhoneEntity phone = PhoneEntity.builder()
                .id(id)
                .number(number)
                .citycode(citycode)
                .contrycode(countrycode)
                .usuario(user)
                .build();

        assertEquals(id, phone.getId());
        assertEquals(number, phone.getNumber());
        assertEquals(citycode, phone.getCitycode());
        assertEquals(countrycode, phone.getContrycode());
        assertEquals(user, phone.getUsuario());
    }

    @Test
    void setters_shouldWorkCorrectly() {
        PhoneEntity phone = new PhoneEntity();

        UUID id = UUID.randomUUID();
        String number = "987654321";
        String citycode = "2";
        String countrycode = "56";
        UserEntity user = new UserEntity();

        phone.setId(id);
        phone.setNumber(number);
        phone.setCitycode(citycode);
        phone.setContrycode(countrycode);
        phone.setUsuario(user);

        assertEquals(id, phone.getId());
        assertEquals(number, phone.getNumber());
        assertEquals(citycode, phone.getCitycode());
        assertEquals(countrycode, phone.getContrycode());
        assertEquals(user, phone.getUsuario());
    }

    @Test
    void noArgsConstructor_shouldInitializeObject() {
        PhoneEntity phone = new PhoneEntity();
        assertNotNull(phone);
    }

    @Test
    void allArgsConstructor_shouldInitializeObject() {
        UUID id = UUID.randomUUID();
        String number = "111222333";
        String citycode = "3";
        String countrycode = "58";
        UserEntity user = new UserEntity();

        PhoneEntity phone = new PhoneEntity(id, number, citycode, countrycode, user);

        assertNotNull(phone);
        assertEquals(id, phone.getId());
        assertEquals(number, phone.getNumber());
        assertEquals(citycode, phone.getCitycode());
        assertEquals(countrycode, phone.getContrycode());
        assertEquals(user, phone.getUsuario());
    }
} 