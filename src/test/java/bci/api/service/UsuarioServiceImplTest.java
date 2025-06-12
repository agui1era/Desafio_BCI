package bci.api.service;

import bci.api.config.JwtUtil;
import bci.api.dto.PhoneRequestDTO;
import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;
import bci.api.entity.PhoneEntity;
import bci.api.entity.UserEntity;
import bci.api.exception.InvalidEmailException;
import bci.api.exception.InvalidPasswordException;
import bci.api.exception.UserAlreadyExistsException;
import bci.api.repository.UserRepository;
import bci.api.serviceImpl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioServiceImpl Tests")
class UsuarioServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UserRequestDTO userRequestDTO;
    private UserEntity userEntity;
    private final String validEmail = "test@example.com";
    private final String validPassword = "Password12";
    private final String mockToken = "mockToken";

    @Captor
    private ArgumentCaptor<UserEntity> userEntityArgumentCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(usuarioService, "passwordEncoder", passwordEncoder);

        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setName("Test User");
        userRequestDTO.setEmail(validEmail);
        userRequestDTO.setPassword(validPassword);
        PhoneRequestDTO phoneReqDTO = new PhoneRequestDTO();
        phoneReqDTO.setNumber("123456789");
        phoneReqDTO.setCitycode("1");
        phoneReqDTO.setContrycode("57");
        userRequestDTO.setPhones(Collections.singletonList(phoneReqDTO));

        userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setNombre(userRequestDTO.getName());
        userEntity.setEmail(userRequestDTO.getEmail());
        userEntity.setPassword(new BCryptPasswordEncoder().encode(validPassword));
        userEntity.setActive(true);
        userEntity.setCreated(LocalDateTime.now().minusDays(1));
        userEntity.setModified(LocalDateTime.now().minusDays(1));
        userEntity.setLastLogin(LocalDateTime.now().minusHours(1));
        userEntity.setToken(mockToken);
        PhoneEntity phoneEntity = new PhoneEntity();
        phoneEntity.setNumber("123456789");
        phoneEntity.setCitycode("1");
        phoneEntity.setContrycode("57");
        userEntity.setPhones(Collections.singletonList(phoneEntity));
    }

    @Test
    void registrarUsuario_success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(anyString())).thenReturn(mockToken);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            savedUser.setToken(mockToken);
            savedUser.setCreated(LocalDateTime.now());
            savedUser.setModified(LocalDateTime.now());
            savedUser.setLastLogin(LocalDateTime.now());
            savedUser.setActive(true);
            return savedUser;
        });

        UserResponseDTO response = usuarioService.registrarUsuario(userRequestDTO);

        assertNotNull(response);
        assertEquals(userRequestDTO.getName(), response.getNombre());
        assertEquals(userRequestDTO.getEmail(), response.getEmail());
        assertTrue(response.isActive());
        assertNotNull(response.getId());
        assertNotNull(response.getCreated());
        assertNotNull(response.getLastLogin());
        assertEquals(mockToken, response.getToken());
        assertFalse(response.getPhones().isEmpty());
        assertEquals(1, response.getPhones().size());
        assertEquals(userRequestDTO.getPhones().get(0).getNumber(), response.getPhones().get(0).getNumber());

        verify(userRepository).findByEmail(validEmail);
        verify(passwordEncoder).encode(validPassword);
        verify(userRepository).save(userEntityArgumentCaptor.capture());
        verify(jwtUtil).generateToken(validEmail);

        UserEntity capturedUser = userEntityArgumentCaptor.getValue();
        assertNotNull(capturedUser.getPassword());
        assertEquals("encodedPassword", capturedUser.getPassword());
        assertEquals(userRequestDTO.getEmail(), capturedUser.getEmail());
        assertEquals(userRequestDTO.getName(), capturedUser.getNombre());
        assertFalse(capturedUser.getPhones().isEmpty());
        assertEquals(1, capturedUser.getPhones().size());
        assertEquals(userRequestDTO.getPhones().get(0).getNumber(), capturedUser.getPhones().get(0).getNumber());
    }

    @Test
    void registrarUsuario_emailAlreadyExists_throwsUserAlreadyExistsException() {
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(userEntity));

        assertThrows(UserAlreadyExistsException.class, () -> {
            usuarioService.registrarUsuario(userRequestDTO);
        });

        verify(userRepository).findByEmail(validEmail);
        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void registrarUsuario_invalidEmailFormat_throwsInvalidEmailException() {
        userRequestDTO.setEmail("invalidemail");

        assertThrows(InvalidEmailException.class, () -> {
            usuarioService.registrarUsuario(userRequestDTO);
        });
        verify(userRepository, never()).findByEmail(anyString());
        verifyNoInteractions(passwordEncoder, jwtUtil, userRepository);
    }

    @Test
    void registrarUsuario_invalidPasswordFormat_TooManyUppercase_throwsInvalidPasswordException() {
        userRequestDTO.setPassword("InvalidPASS12");

        assertThrows(InvalidPasswordException.class, () -> {
            usuarioService.registrarUsuario(userRequestDTO);
        });
        verifyNoInteractions(userRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void registrarUsuario_invalidPasswordFormat_TooManyNumbers_throwsInvalidPasswordException() {
        userRequestDTO.setPassword("Password123");
        assertThrows(InvalidPasswordException.class, () -> {
            usuarioService.registrarUsuario(userRequestDTO);
        });
        verifyNoInteractions(userRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void registrarUsuario_invalidPasswordFormat_TooShort_throwsInvalidPasswordException() {
        userRequestDTO.setPassword("Pass12");
        assertThrows(InvalidPasswordException.class, () -> {
            usuarioService.registrarUsuario(userRequestDTO);
        });
        verifyNoInteractions(userRepository, jwtUtil, passwordEncoder);
    }

    @Test
    void registrarUsuario_success_noPhones() {
        userRequestDTO.setPhones(Collections.emptyList());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(anyString())).thenReturn(mockToken);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            savedUser.setToken(mockToken);
            savedUser.setCreated(LocalDateTime.now());
            savedUser.setModified(LocalDateTime.now());
            savedUser.setLastLogin(LocalDateTime.now());
            savedUser.setActive(true);
            return savedUser;
        });

        UserResponseDTO response = usuarioService.registrarUsuario(userRequestDTO);
        assertNotNull(response);
        assertEquals(userRequestDTO.getName(), response.getNombre());
        assertTrue(response.getPhones().isEmpty());
        verify(userRepository).save(any(UserEntity.class));
        verify(passwordEncoder).encode(validPassword);
    }

    @Test
    void login_invalidToken_throwsRuntimeException() {
        String invalidToken = "invalidOrExpiredToken";
        when(jwtUtil.validateToken(invalidToken)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.login(invalidToken);
        });
        assertEquals("Token inválido o expirado", exception.getMessage());
        verify(jwtUtil).validateToken(invalidToken);
        verifyNoMoreInteractions(jwtUtil);
        verifyNoInteractions(userRepository);
    }

    @Test
    void login_userNotFound_throwsRuntimeException() {
        String token = "validToken";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(validEmail);
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.login(token);
        });
        assertEquals("Usuario no encontrado para el token proporcionado", exception.getMessage());
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).getUsernameFromToken(token);
        verify(userRepository).findByEmail(validEmail);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void login_success() {
        String token = "validToken";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getUsernameFromToken(token)).thenReturn(validEmail);
        when(userRepository.findByEmail(validEmail)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserResponseDTO response = usuarioService.login(token);

        assertNotNull(response);
        assertEquals(userEntity.getId().toString(), response.getId());
        assertEquals(userEntity.getEmail(), response.getEmail());
        assertEquals(userEntity.getToken(), response.getToken());
        verify(jwtUtil).validateToken(token);
        verify(jwtUtil).getUsernameFromToken(token);
        verify(userRepository).findByEmail(validEmail);
        verify(userRepository).save(any(UserEntity.class));
    }
}