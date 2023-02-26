package com.cachew.accountmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteResponseDTO extends DTO {

    private boolean isDeleted;

}
