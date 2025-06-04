package bci.api.service;

import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;

public interface UsuarioService {
    UserResponseDTO registrarUsuario(UserRequestDTO request);
    UserResponseDTO login(String token);
}