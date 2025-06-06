package bci.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailDTO {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;
}