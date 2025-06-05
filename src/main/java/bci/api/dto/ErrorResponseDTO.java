package bci.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ErrorResponseDTO {
    private List<ErrorDetailDTO> error;
}