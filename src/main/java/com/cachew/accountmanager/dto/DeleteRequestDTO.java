package com.cachew.accountmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequestDTO extends DTO {

    private String username;
    private String password;

}
