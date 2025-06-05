package bci.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ErrorDetailDTO {
    private LocalDateTime timestamp;
    private int codigo;
    private String detail;
}