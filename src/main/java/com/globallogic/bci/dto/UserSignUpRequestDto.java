package main.java.com.globallogic.bci.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id; // [cite: 13]
    private LocalDateTime created; // [cite: 14]
    private LocalDateTime lastLogin; // [cite: 14]
    private String token; // [cite: 14]
    private boolean isActive; // [cite: 14]
    private String name;
    private String email;
    private String password; // Per requirement[cite: 19], though typically not returned.
    private List<PhoneDto> phones;
}