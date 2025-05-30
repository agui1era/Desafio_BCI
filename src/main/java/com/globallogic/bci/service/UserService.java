package main.java.com.globallogic.bci.service;

import com.globallogic.bci.dto.UserSignUpRequestDto;
import com.globallogic.bci.dto.UserResponseDto;
import com.globallogic.bci.dto.PhoneDto;
import com.globallogic.bci.entity.User;
import com.globallogic.bci.entity.Phone;
import com.globallogic.bci.exception.InvalidTokenException;
import com.globallogic.bci.exception.UserAlreadyExistsException;
import com.globallogic.bci.exception.UserNotFoundException;
import com.globallogic.bci.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime; // Java 8 Date/Time API [cite: 6]
import java.util.Collections;
import java.util.List;
import java.util.Optional; // Java 8 Optional [cite: 6]
import java.util.UUID;
import java.util.stream.Collectors; // Java 8 Stream API [cite: 6]

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param requestDto DTO con la información para el registro del usuario.
     * @return UserResponseDto con la información del usuario creado y el token JWT.
     * @throws UserAlreadyExistsException si el correo electrónico ya está registrado. [cite: 17]
     */
    @Transactional
    public UserResponseDto createUser(UserSignUpRequestDto requestDto) {
        // La validación de formato de email y contraseña se asume manejada por anotaciones en el DTO.
        // Requisito de validación de email [cite: 10, 11]
        // Requisito de validación de contraseña [cite: 11, 12]

        Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("El correo ya registrado"); // [cite: 17]
        }

        User user = new User();
        user.setName(requestDto.getName()); // [cite: 13] (opcional)
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword())); // Encriptación de contraseña [cite: 16]

        LocalDateTime now = LocalDateTime.now();
        user.setCreated(now); // [cite: 14]
        user.setLastLogin(now); // [cite: 14]
        user.setActive(true); // [cite: 14]
        // user.setId() se genera automáticamente por @PrePersist o estrategia de generación de UUID en la entidad. [cite: 13]

        if (requestDto.getPhones() != null && !requestDto.getPhones().isEmpty()) { // [cite: 13] (opcional)
            List<Phone> phones = requestDto.getPhones().stream() // Uso de Stream API [cite: 6]
                .map(dto -> {
                    Phone phone = new Phone();
                    phone.setNumber(dto.getNumber());
                    phone.setCitycode(dto.getCitycode());
                    phone.setContrycode(dto.getContrycode());
                    // La relación con User se establece por la cascada y JoinColumn en la entidad User
                    return phone;
                })
                .collect(Collectors.toList());
            user.setPhones(phones);
        } else {
            user.setPhones(Collections.emptyList());
        }
        
        // Generar token JWT después de que el usuario tenga un ID (si es necesario para el payload del token)
        // En este caso, generamos el token y luego lo guardamos con el usuario.
        User preSavedUser = userRepository.saveAndFlush(user); // Guardar para obtener ID si es necesario y para consistencia antes del token.
        
        String token = jwtService.generateToken(preSavedUser.getEmail(), preSavedUser.getId()); // [cite: 14]
        preSavedUser.setToken(token);

        User savedUser = userRepository.save(preSavedUser); // Persistencia en BD H2 usando Spring Data [cite: 15]

        return mapUserToUserResponseDto(savedUser, savedUser.getPassword()); // Se pasa la contraseña original (encriptada) según formato de respuesta.
    }

    /**
     * Realiza el login de un usuario utilizando un token JWT.
     * Actualiza la fecha de último login y el token del usuario.
     *
     * @param authorizationHeader El encabezado de autorización que contiene el token JWT (ej: "Bearer <token>").
     * @return UserResponseDto con la información actualizada del usuario, incluyendo el nuevo token.
     * @throws UserNotFoundException si el usuario asociado al token no se encuentra.
     * @throws InvalidTokenException si el token es inválido o ha expirado.
     */
    @Transactional
    public UserResponseDto loginUser(String authorizationHeader) { // [cite: 18]
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException("Formato de token inválido o ausente.");
        }
        String token = authorizationHeader.substring(7);

        if (!jwtService.validateToken(token)) {
             throw new InvalidTokenException("Token inválido o expirado.");
        }

        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));

        user.setLastLogin(LocalDateTime.now()); // Actualizar fecha de último login
        String newToken = jwtService.generateToken(user.getEmail(), user.getId()); // Generar nuevo token [cite: 18]
        user.setToken(newToken); // Actualizar token del usuario

        User updatedUser = userRepository.save(user);

        // El contrato de salida especifica el campo password. [cite: 19]
        // En un escenario real, se debe tener cuidado al devolver contraseñas, incluso encriptadas.
        // Aquí se devuelve la contraseña encriptada almacenada.
        return mapUserToUserResponseDto(updatedUser, updatedUser.getPassword());
    }


    /**
     * Mapea una entidad User a UserResponseDto.
     *
     * @param user La entidad User a mapear.
     * @param passwordToInclude La contraseña (generalmente encriptada) a incluir en el DTO según los requisitos.
     * @return UserResponseDto poblado con los datos del usuario.
     */
    private UserResponseDto mapUserToUserResponseDto(User user, String passwordToInclude) {
        List<PhoneDto> phoneDtos = (user.getPhones() != null)
            ? user.getPhones().stream().map(phone -> { // Uso de lambda y Stream API [cite: 6]
                PhoneDto dto = new PhoneDto();
                dto.setNumber(phone.getNumber());
                dto.setCitycode(phone.getCitycode());
                dto.setContrycode(phone.getContrycode());
                return dto;
            }).collect(Collectors.toList())
            : Collections.emptyList();

        return UserResponseDto.builder()
                .id(user.getId()) // [cite: 13]
                .created(user.getCreated()) // [cite: 14]
                .lastLogin(user.getLastLogin()) // [cite: 14, 19]
                .token(user.getToken()) // [cite: 14, 19]
                .isActive(user.isActive()) // [cite: 14, 19]
                .name(user.getName()) // [cite: 19]
                .email(user.getEmail()) // [cite: 19]
                .password(passwordToInclude) // Contraseña (encriptada) según formato de respuesta. [cite: 19]
                .phones(phoneDtos) // [cite: 19]
                .build();
    }
}