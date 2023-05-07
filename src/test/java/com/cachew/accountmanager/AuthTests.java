package com.cachew.accountmanager;

import com.cachew.accountmanager.dto.*;
import com.cachew.accountmanager.web.AuthController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper mapper;

//    @Test
//    public void registerTest() throws JsonProcessingException {
//        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user23", "user23");
//        ResponseEntity<String> result = authController.register(registerRequestDTO);
//
//        RegisterResponseDTO dto = mapper.readValue(result.getBody(), RegisterResponseDTO.class);
//
//        assertNotNull("dto is null", dto);
////        assertEquals("is not sign up", 200, dto.getException().getCode());
////        assertEquals("User is registered", "Ok", dto.getException().getDescription());
//        log.info("token:\n{}", dto.getToken().toString());
//    }
//
//    @Test
//    public void loginTest() throws JsonProcessingException {
//        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user23", "user23");
//        authController.register(registerRequestDTO);
//
//        LoginRequestDTO loginDTO = new LoginRequestDTO("user23", "user23");
//        ResponseEntity<String> result = authController.login(loginDTO);
//
//        LoginResponseDTO dto = mapper.readValue(result.getBody(), LoginResponseDTO.class);
//
//        assertNotNull("dto is null", dto);
//        assertEquals("username not user", "user23", dto.getUsername());
//        assertNotNull("token is not null", dto.getToken());
//    }
//
//    @Test
//    public void refreshTest() throws JsonProcessingException {
//        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user23", "user23");
//        authController.register(registerRequestDTO);
//
//        LoginRequestDTO loginDTO = new LoginRequestDTO("user23", "user23");
//        ResponseEntity<String> result = authController.login(loginDTO);
//        LoginResponseDTO loginResponseDTO = mapper.readValue(result.getBody(), LoginResponseDTO.class);
//
//        String refreshToken = loginResponseDTO.getToken().getRefreshToken();
//        String id = loginResponseDTO.getId();
//
//        TokenRefreshDTO tokenRefreshDTO = new TokenRefreshDTO(id, refreshToken);
//
//        String token = authController.token(tokenRefreshDTO).getBody();
//
//        assertNotNull("refresh token is null" , loginResponseDTO.getToken().getRefreshToken());
//        assertNotNull("userId is null", loginResponseDTO.getToken().getUserId());
//
//        assertNotNull("refreshed token is null", token);
//        log.info("token:\n{}", token.toString());
//    }
//
//    @Test
//    public void checkTokenTest() {
//        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO("user23", "user23");
//        ResponseEntity<String> register = authController.register(registerRequestDTO);
//        String token = register.getBody();
//
//        log.info(token);
//
//        log.info("token: {}", token);
//
//    }

    @Test
    public void mapperDTO() {
        StatusDTO status = new StatusDTO(100, "description");
        assertEquals("", "{\n  \"code\" : 100,\n  \"description\" : \"description\"\n}", status.toJson());
    }

}
