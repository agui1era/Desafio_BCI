package bci.api.controller;

import bci.api.dto.UserRequestDTO;
import bci.api.model.Usurious;
import bci.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("sign-up")
    public ResponseEntity<Usurious> registrar(@RequestBody UserRequestDTO request) {
        Usurious usuario = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("login")
    public ResponseEntity<Usurious> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().build();
        }
        String token = authHeader.substring(7);
        Usurious usuario = usuarioService.loginUsuario(token);
        return ResponseEntity.ok(usuario);
    }
}