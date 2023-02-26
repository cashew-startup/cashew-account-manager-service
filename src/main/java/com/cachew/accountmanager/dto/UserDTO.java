package com.cachew.accountmanager.dto;

import com.cachew.accountmanager.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO extends DTO {
    private String id;
    private String username;

    public static UserDTO from(User user) {
        return builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .build();
    }
}
