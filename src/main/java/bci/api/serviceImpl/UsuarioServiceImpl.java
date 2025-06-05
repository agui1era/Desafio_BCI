package bci.api.serviceImpl;

import bci.api.config.JwtUtil;
import bci.api.dto.PhoneDTO;
import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;
import bci.api.entity.PhoneEntity;
import bci.api.entity.UserEntity;
import bci.api.exception.InvalidEmailException;
import bci.api.exception.InvalidPasswordException;
import bci.api.exception.UserAlreadyExistsException;
import bci.api.repository.UserRepository;
import bci.api.service.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?!.*[A-Z].*[A-Z])(?=.*[0-9].*[0-9])(?!.*[0-9].*[0-9].*[0-9])[a-zA-Z0-9]{8,12}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);


    @Override
    @Transactional
    public UserResponseDTO registrarUsuario(UserRequestDTO request) {
        logger.debug("Iniciando registro de usuario para email: {}", request.getEmail());

        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            logger.warn("Falló validación de email para: {}. Formato incorrecto.", request.getEmail());
            throw new InvalidEmailException("El formato del correo no es válido. Debe ser aaaaaaa@undominio.algo");
        }

        if (!PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            logger.warn("Falló validación de contraseña para email: {}. Formato incorrecto.", request.getEmail());
            throw new InvalidPasswordException("El formato de la clave no es válido. Debe tener solo una Mayúscula y solamente dos números (no necesariamente consecutivos), en combinación de letras minúsculas, largo máximo de 12 y mínimo 8.");
        }

        Optional<UserEntity> existente = userRepository.findByEmail(request.getEmail());
        if (existente.isPresent()) {
            logger.info("Intento de registro de email ya existente: {}", request.getEmail());
            throw new UserAlreadyExistsException("El correo ya está registrado");
        }

        UserEntity user = new UserEntity();
        user.setId(UUID.randomUUID());
        user.setNombre(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);
        user.setCreated(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());

        if (request.getPhones() != null && !request.getPhones().isEmpty()) {
            user.setPhones(
                    request.getPhones().stream().map(phoneDto -> {
                        PhoneEntity phone = new PhoneEntity();
                        phone.setNumber(phoneDto.getNumber());
                        phone.setCitycode(phoneDto.getCitycode());
                        phone.setContrycode(phoneDto.getContrycode());
                        phone.setUsuario(user);
                        return phone;
                    }).collect(Collectors.toList())
            );
            logger.debug("Teléfonos procesados y asociados al usuario: {}", request.getEmail());
        } else {
            logger.debug("No se proporcionaron teléfonos para el usuario: {}", request.getEmail());
        }
        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);
        logger.debug("Token JWT generado para el usuario: {}", request.getEmail());

        UserEntity saved = userRepository.save(user);
        logger.info("Usuario registrado exitosamente con ID: {} y email: {}", saved.getId(), saved.getEmail());

        return UserResponseDTO.builder()
                .id(saved.getId().toString())
                .created(saved.getCreated())
                .lastLogin(saved.getLastLogin())
                .token(saved.getToken())
                .isActive(saved.isActive())
                .nombre(saved.getNombre())
                .email(saved.getEmail())
                .phones(saved.getPhones() != null ? saved.getPhones().stream()
                        .map(phoneEntity -> new PhoneDTO(
                                phoneEntity.getNumber(),
                                phoneEntity.getCitycode(),
                                phoneEntity.getContrycode()
                        )).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
    @Override
    public UserResponseDTO login(String token) {
        logger.debug("Iniciando proceso de login/validación de token.");
        if (!jwtUtil.validateToken(token)) {
            logger.warn("Token JWT inválido o expirado detectado durante el login.");
            throw new RuntimeException("Token inválido o expirado");
        }
        String email = jwtUtil.getSubject(token);
        logger.debug("Email extraído del token: {}", email);
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            logger.error("Usuario con email {} no encontrado en la base de datos.", email);
            throw new RuntimeException("Usuario no encontrado para el token proporcionado");
        }
        UserEntity user = userOpt.get();
        logger.debug("Usuario {} encontrado. Actualizando lastLogin y generando nuevo token.", email);
        user.setLastLogin(LocalDateTime.now());
        user.setModified(LocalDateTime.now());
        String nuevoToken = jwtUtil.generateToken(user.getEmail());
        user.setToken(nuevoToken);
        userRepository.save(user);
        logger.info("Login exitoso para usuario {}. lastLogin actualizado y nuevo token generado.", email);

        return UserResponseDTO.builder()
                .id(user.getId().toString())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .phones(user.getPhones() != null ? user.getPhones().stream()
                        .map(phoneEntity -> new PhoneDTO(
                                phoneEntity.getNumber(),
                                phoneEntity.getCitycode(),
                                phoneEntity.getContrycode()
                        )).collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
}