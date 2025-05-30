package main.java.com.globallogic.bci.dto;

import lombok.Data;
import java.util.List;

@Data
public class ErrorResponseDto {
    private List<ErrorDetailDto> error; // [cite: 18]
}   