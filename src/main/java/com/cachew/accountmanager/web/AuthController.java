package com.cachew.accountmanager.web;

import com.cachew.accountmanager.dto.*;
import com.cachew.accountmanager.entity.User;
import com.cachew.accountmanager.service.MainUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        RegisterResponseDTO isCreated = mainUserService.createUser(user);

        return ResponseEntity.ok(isCreated);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        LoginResponseDTO login = mainUserService.loginUser(loginDTO);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDTO> token(@RequestBody TokenRefreshDTO tokenDTO) {
        return mainUserService.refreshToken(tokenDTO);
    }

}
