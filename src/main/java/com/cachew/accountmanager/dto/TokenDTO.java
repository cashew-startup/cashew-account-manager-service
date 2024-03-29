package com.cachew.accountmanager.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenDTO extends DTO {

    private String userId;
    private String username;
    private String accessToken;
    private String refreshToken;

}
