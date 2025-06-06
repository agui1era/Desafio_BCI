package bci.api.controller;

import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;
import bci.api.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UserController userController;

    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setPassword("Password123");
        // Puedes inicializar más campos de UserRequestDTO si es necesario

        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(UUID.randomUUID());
        userResponseDTO.setToken("mockToken");
        // Puedes inicializar más campos de UserResponseDTO si es necesario
    }

    @Test
    void registerUser_shouldReturnCreatedResponse_whenUserIsRegisteredSuccessfully() {
        // Arrange
        when(usuarioService.registrarUsuario(any(UserRequestDTO.class))).thenReturn(userResponseDTO);

        // Act
        ResponseEntity<UserResponseDTO> responseEntity = userController.registerUser(userRequestDTO);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userResponseDTO.getId(), responseEntity.getBody().getId());
        assertEquals(userResponseDTO.getToken(), responseEntity.getBody().getToken());

        verify(usuarioService).registrarUsuario(any(UserRequestDTO.class));
    }

    @Test
    void loginUser_shouldReturnOkResponse_whenTokenIsValid() {
        // Arrange
        String token = "validToken";
        when(usuarioService.login(token)).thenReturn(userResponseDTO);

        // Act
        ResponseEntity<UserResponseDTO> responseEntity = userController.loginUser(token);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userResponseDTO.getId(), responseEntity.getBody().getId());
        // Puedes añadir más aserciones para otros campos de UserResponseDTO

        verify(usuarioService).login(token);
    }
}