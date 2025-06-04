package bci.api.serviceImpl;

import bci.api.config.JwtUtil;
import bci.api.dto.PhoneRequestDTO;
import bci.api.dto.UserRequestDTO;
import bci.api.dto.UserResponseDTO;
import bci.api.model.Phone;
import bci.api.repository.UserRepository;
import bci.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import bci.api.entity.User;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public UserResponseDTO registrarUsuario(UserRequestDTO request) {
        Optional<User> existente = userRepository.findByEmail(request.getEmail());
        if (existente.isPresent()) {
            throw new RuntimeException("El correo ya está registrado");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setActive(true);

        if (request.getPhones() != null) {
            user.setPhones(
                    request.getPhones().stream().map(phoneDto -> {
                        Phone phone = new Phone();
                        phone.setNumber(phoneDto.getNumber());
                        phone.setCitycode(phoneDto.getCitycode());
                        phone.setContrycode(phoneDto.getContrycode());
                        phone.setUsuario(user);
                        return phone;
                    }).collect(Collectors.toList())
            );
        }

        String token = jwtUtil.generateToken(user.getEmail());
        user.setToken(token);

        User saved = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(saved.getId().toString())
                .lastLogin(saved.getLastLogin())
                .token(saved.getToken())
                .isActive(saved.isActive())
                .email(saved.getEmail())
                .password(saved.getPassword())
                .phones(saved.getPhones())
                .build();
    }

    @Override
    public UserResponseDTO login(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }

        String email = jwtUtil.getSubject(token);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        User user = userOpt.get();
        user.setLastLogin(Instant.now());
        String nuevoToken = jwtUtil.generateToken(user.getEmail());
        user.setToken(nuevoToken);
        userRepository.save(user);

        return UserResponseDTO.builder()
                .id(user.getId().toString())
                .created(user.getCreated())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isActive(user.isActive())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phones(user.getPhones())
                .build();
    }
}