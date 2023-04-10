package com.cachew.accountmanager.web;

import com.cachew.accountmanager.dto.*;
import com.cachew.accountmanager.entity.User;
import com.cachew.accountmanager.service.MainUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    MainUserService mainUserService;

    @Autowired
    public AuthController(MainUserService mainUserService) {
        this.mainUserService = mainUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        User user = new User(registerRequestDTO.getUsername(), registerRequestDTO.getPassword());
        return mainUserService.createUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return mainUserService.loginUser(loginRequestDTO);
    }

    @GetMapping("/delete/{password}")
    public ResponseEntity<String> delete(@AuthenticationPrincipal User user, String password) {
        return mainUserService.deleteUser(user, password);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<String> token(@RequestBody TokenRefreshDTO tokenDTO) {
        return mainUserService.refreshToken(tokenDTO);
    }

    @GetMapping("/unauthorized")
    public ResponseEntity<AuthenticationDTO> getUnauthorized() {
        return ResponseEntity.ok(new AuthenticationDTO(null, "anonymous", "false"));
    }

    @GetMapping("/token/validate")
    public ResponseEntity<AuthenticationDTO> validateToken(Authentication authentication) {

        if (authentication == null) return ResponseEntity.ok(new AuthenticationDTO(null, "anonymous", "false"));

        User userDetails = (User) authentication.getPrincipal();
        Long id = userDetails.getId();
        return ResponseEntity.ok(new AuthenticationDTO(id, "USER", "true"));
    }

}
