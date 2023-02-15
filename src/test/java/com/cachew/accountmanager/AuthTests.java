package com.cachew.accountmanager;

import com.cachew.accountmanager.dto.*;
import com.cachew.accountmanager.web.AuthController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.util.AssertionErrors.*;

@Slf4j
@SpringBootTest
@Transactional
public class AuthTests {

    @Autowired
    private AuthController authController;

    @Test
    public void registerTest() {
        SignupDTO signupDTO = new SignupDTO("user", "user");
        ResponseEntity<RegisterResponseDTO> result = authController.register(signupDTO);

        RegisterResponseDTO dto = result.getBody();

        assertNotNull("dto is null", dto);
        assertEquals("is not sign up", 200, dto.getException().getCode());
        assertEquals("User is registered", "Ok", dto.getException().getDescription());
        log.info("token:\n{}", dto.getToken().toString());
    }

    @Test
    public void loginTest() {
        SignupDTO signupDTO = new SignupDTO("user", "user");
        authController.register(signupDTO);

        LoginDTO loginDTO = new LoginDTO("user", "user");
        ResponseEntity<LoginResponseDTO> result = authController.login(loginDTO);

        LoginResponseDTO dto = result.getBody();

        assertNotNull("dto is null", dto);
        assertEquals("username not user", "user", dto.getUsername());
        assertNotNull("token is not null", dto.getToken());
    }

    @Test
    public void refreshTest() {
        SignupDTO signupDTO = new SignupDTO("user", "user");
        authController.register(signupDTO);

        LoginDTO loginDTO = new LoginDTO("user", "user");
        ResponseEntity<LoginResponseDTO> result = authController.login(loginDTO);
        TokenDTO tokenDTO = result.getBody().getToken();

        String refreshToken = tokenDTO.getRefreshToken();
        String id = tokenDTO.getUserId();

        TokenRefreshDTO tokenRefreshDTO = new TokenRefreshDTO(id, refreshToken);

        TokenDTO token = authController.token(tokenRefreshDTO).getBody();

        assertNotNull("refresh token is null" , tokenDTO.getRefreshToken());
        assertNotNull("userId is null", tokenDTO.getUserId());

        assertNotNull("refreshed token is null", token);
        log.info("token:\n{}", token.toString());
    }

    @Test
    public void checkTokenTest() {
        SignupDTO signupDTO = new SignupDTO("user", "user");
        ResponseEntity<RegisterResponseDTO> register = authController.register(signupDTO);
        TokenDTO token = register.getBody().getToken();

        String refresh = token.getRefreshToken();
        String access = token.getAccessToken();

        log.info("refresh token: {}", refresh);
        log.info("access token: {}", access);

    }

}
