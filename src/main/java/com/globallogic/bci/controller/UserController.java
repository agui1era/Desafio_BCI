package main.java.com.globallogic.bci.controller;

import com.globallogic.bci.dto.UserSignUpRequestDto;
import com.globallogic.bci.dto.UserResponseDto;
import com.globallogic.bci.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE) // Asegura que todas las respuestas sean JSON por defecto
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint para la creación de un usuario.
     * @param requestDto Datos del usuario para el registro.
     * @return ResponseEntity con UserResponseDto en caso de éxito o un ErrorResponseDto en caso de error.
     */
    @PostMapping(value = "/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE) // [cite: 8, 10]
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Endpoint para consultar el usuario utilizando un token.
     * El token debe ser provisto en el header "Authorization" como "Bearer <token>".
     * Este endpoint actualiza el token del usuario y retorna toda su información.
     * @param authorizationHeader El token JWT para autenticación y consulta.
     * @return ResponseEntity con UserResponseDto en caso de éxito o un ErrorResponseDto en caso de error.
     */
    @GetMapping(value = "/login") // [cite: 18]
    public ResponseEntity<UserResponseDto> login(@RequestHeader("Authorization") String authorizationHeader) {
        // Se espera que el token venga en el formato "Bearer <token>"
        // El servicio se encargará de procesar el header, validar y actualizar el token.
        UserResponseDto userResponseDto = userService.loginUser(authorizationHeader);
        return ResponseEntity.ok(userResponseDto); // [cite: 19]
    }
}