package bci.api.controller;

import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;
import bci.api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UsuarioService usuarioService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDTO> registrarUsuario(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO response = usuarioService.registrarUsuario(userRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        UserResponseDTO response = usuarioService.login(token);
        return ResponseEntity.ok(response);
    }
}