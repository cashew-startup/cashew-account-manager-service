package com.cachew.accountmanager.web;

import com.cachew.accountmanager.dto.DeleteUserDTO;
import com.cachew.accountmanager.entity.User;
import com.cachew.accountmanager.dto.UserDTO;
import com.cachew.accountmanager.repository.UserRepository;
import com.cachew.accountmanager.service.MainUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    MainUserService mainUserService;

    @Autowired
    public UserController(MainUserService mainUserService) {
        this.mainUserService = mainUserService;
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<DeleteUserDTO> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        DeleteUserDTO deleteUserDTO = mainUserService.deleteUser(user, id);
        return ResponseEntity.ok(deleteUserDTO);
    }

    @GetMapping("/{id}")
    @PreAuthorize("#user.id == #id")
    public ResponseEntity user(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(UserDTO.from(userRepository.findById(id).orElseThrow()));
    }

    @GetMapping("/info")
    public ResponseEntity getUserInfo(@AuthenticationPrincipal User user) {
        System.out.println(user.toString());
        return ResponseEntity.ok(true);
    }

}
