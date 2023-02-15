package com.cachew.accountmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {

    private String id;
    private String username;
    private TokenDTO token;
    private ExceptionDTO exception;

}
