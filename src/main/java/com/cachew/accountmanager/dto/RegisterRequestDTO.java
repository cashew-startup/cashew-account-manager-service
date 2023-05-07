package com.cachew.accountmanager.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO extends DTO {

    private String email;
    private String username;
    private String password;

}
