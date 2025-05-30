package main.java.com.globallogic.bci.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDetailDto {
    private LocalDateTime timestamp; // [cite: 18]
    private int codigo; // [cite: 18]
    private String detail; // [cite: 18]
}