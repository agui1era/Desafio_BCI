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

import java.time.LocalDateTime; // Import LocalDateTime if you plan to set it
import java.util.Collections; // Import Collections for empty list
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

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
        userRequestDTO.setName("Test User"); // Assuming name is part of UserRequestDTO
        userRequestDTO.setEmail("test@example.com");
        userRequestDTO.setPassword("Password123");
        userRequestDTO.setPhones(Collections.emptyList()); // Initialize phones if needed

        // Option 1: Using constructor and setters (now that @NoArgsConstructor is added)
        userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(UUID.randomUUID().toString()); // Convert UUID to String
        userResponseDTO.setToken("mockToken");
        userResponseDTO.setCreated(LocalDateTime.now());
        userResponseDTO.setLastLogin(LocalDateTime.now());
        userResponseDTO.setActive(true);
        userResponseDTO.setNombre("Test User Name");
        userResponseDTO.setEmail("test@example.com");
        userResponseDTO.setPhones(Collections.emptyList());


        // Option 2: Using the builder (often preferred with @Builder)
        /*
        userResponseDTO = UserResponseDTO.builder()
                .id(UUID.randomUUID().toString()) // Convert UUID to String
                .token("mockToken")
                .created(LocalDateTime.now())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .nombre("Test User Name")
                .email("test@example.com")
                .phones(Collections.emptyList()) // Assuming PhoneDTO is also set up or mocked
                .build();
        */
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
        String authHeader = "Bearer validToken";
        when(usuarioService.login(anyString())).thenReturn(userResponseDTO);

        ResponseEntity<UserResponseDTO> responseEntity = userController.loginUser(authHeader);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(userResponseDTO.getId(), responseEntity.getBody().getId());

        verify(usuarioService).login("validToken");
    }

    @Test
    void loginUser_shouldReturnUnauthorized_whenHeaderIsMissing() {
        ResponseEntity<UserResponseDTO> responseEntity = userController.loginUser(null);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verifyNoInteractions(usuarioService);
    }

    @Test
    void loginUser_shouldReturnUnauthorized_whenHeaderIsInvalid() {
        ResponseEntity<UserResponseDTO> responseEntity = userController.loginUser("InvalidHeader");

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        verifyNoInteractions(usuarioService);
    }
}