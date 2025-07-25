package com.example.apartment_manager.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonResponse <T>{
    private int code;
    private String message;
    private T data;
}
