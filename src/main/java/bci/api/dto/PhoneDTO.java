package bci.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class PhoneDTO {
    private String number;
    private String citycode;
    private String contrycode;
}