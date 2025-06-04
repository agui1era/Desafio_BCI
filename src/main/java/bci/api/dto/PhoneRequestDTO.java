package bci.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneRequestDTO {
    private Long number;
    private Integer citycode;
    private String contrycode;
}