package bci.api.service;
import bci.api.dto.UserResponseDTO;
import bci.api.dto.UserRequestDTO;

public interface UsuarioService {
    UserResponseDTO registrarUsuario(UserRequestDTO request);
    UserResponseDTO login(String token);
}