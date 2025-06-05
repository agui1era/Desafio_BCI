package bci.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneRequestDTO {
    private String number;
    private String citycode;
    private String contrycode;
}