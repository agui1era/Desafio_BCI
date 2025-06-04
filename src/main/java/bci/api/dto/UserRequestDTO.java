package bci.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
            regexp = "^(?=(?:.*\\d){2})(?=(?:[^A-Z]*[A-Z]){1})(?=^[a-zA-Z\\d]{8,12}$)[a-zA-Z\\d]+$",
            message = "La contraseña debe tener entre 8 y 12 caracteres, 1 mayúscula y 2 números"
    )
    private String password;

    private List<PhoneRequest> phones;
}