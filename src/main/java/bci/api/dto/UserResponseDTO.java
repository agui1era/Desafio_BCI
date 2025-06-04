package bci.api.dto;

import bci.api.model.Phone;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
public class UserResponseDTO {
    private String id;
    private Instant created;
    private Instant lastLogin;
    private String token;
    private boolean isActive;
    private String name;
    private String email;
    private String password;
    private List<Phone> phones;
}