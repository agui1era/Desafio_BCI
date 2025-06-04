package bci.api.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDTO {
    private String name;
    private String email;
    private String password;
    private List<PhoneRequestDTO> phones;
}