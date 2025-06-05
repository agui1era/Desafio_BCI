package bci.api.controller;

import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;
import bci.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UsuarioService usuarioService;

    @Autowired
    public UserController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDTO> registerUser(@RequestBody UserRequestDTO request) {
        logger.info("Recibida petición de registro de usuario para email: {}", request.getEmail());
        UserResponseDTO response = usuarioService.registrarUsuario(request);
        logger.info("Usuario registrado exitosamente con ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@RequestBody String token) {
        logger.info("Recibida petición de login/validación de token.");
        UserResponseDTO response = usuarioService.login(token);
        logger.info("Login/Validación de token exitosa para usuario con ID: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}