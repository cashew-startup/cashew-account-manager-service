package com.cachew.accountmanager.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO extends DTO {

    private String username;
    private String password;

}
