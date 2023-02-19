package com.cachew.accountmanager.web;

import com.cachew.accountmanager.dto.*;
import com.cachew.accountmanager.entity.User;
import com.cachew.accountmanager.service.MainUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody SignupDTO signupDTO) {

        User user = new User(signupDTO.getUsername(), signupDTO.getPassword());

        RegisterResponseDTO response = mainUserService.createUser(user);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        return mainUserService.loginUser(loginDTO);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenRefreshDTO tokenDTO) {
        return mainUserService.refreshToken(tokenDTO);
    }

    @GetMapping("/token/validate")
    public ResponseEntity<AuthenticationDTO> validateToken(Authentication authentication) {

        if (authentication == null) return  ResponseEntity.ok(new AuthenticationDTO(null, "anonymous", "false"));

        User userDetails = (User) authentication.getPrincipal();
        Long id = userDetails.getId();
        return ResponseEntity.ok(new AuthenticationDTO(id, "USER", "true"));
    }

}
