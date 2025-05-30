package com.globallogic.bci.dto;

import lombok.Data;

@Data
public class PhoneDto {
    private long number; // [cite: 10]
    private int citycode; // [cite: 10]
    private String contrycode; // [cite: 10]
}