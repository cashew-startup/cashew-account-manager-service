package com.cachew.accountmanager.service;

import com.cachew.accountmanager.dto.*;
import com.cachew.accountmanager.entity.CustomUserDetails;
import com.cachew.accountmanager.entity.User;
import com.cachew.accountmanager.repository.UserRepository;
import com.cachew.accountmanager.security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
public class MainUserService {

    private UserManager userDetailsManager;

    private TokenGenerator tokenGenerator;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    private JwtAuthenticationProvider refreshTokenAuthProvider;

    private UserRepository userRepository;

    @Autowired
    public MainUserService(UserManager userManager,
                           TokenGenerator tokenGenerator,
                           DaoAuthenticationProvider daoAuthenticationProvider,
                           UserRepository userRepository) {
        this.userDetailsManager = userManager;
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.userRepository = userRepository;
    }

    public RegisterResponseDTO createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            ExceptionDTO exception = new ExceptionDTO();
            exception.setCode(409);
            exception.setDescription("User already exists");
            return new RegisterResponseDTO(String.valueOf(user.getId()), user.getUsername(), null, exception);
        }

        userDetailsManager.createUser(user);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), Collections.EMPTY_LIST);

        ExceptionDTO exception = new ExceptionDTO();
        exception.setCode(200);
        exception.setDescription("Ok");
        return new RegisterResponseDTO(String.valueOf(user.getId()), user.getUsername(), tokenGenerator.createToken(authentication), exception);
    }

    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        Authentication authentication = null;
        try {
            authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (Exception e) {
            ExceptionDTO exception = new ExceptionDTO();
            exception.setDescription("403");
            exception.setDescription("Unauthorized");
            return new LoginResponseDTO(null, loginDTO.getUsername(), null, exception);
        }

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        return new LoginResponseDTO(String.valueOf(details.getId()), details.getUsername(), tokenGenerator.createToken(authentication), null);
    }

    public ResponseEntity<TokenDTO> refreshToken(TokenRefreshDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();
        long id = Long.parseLong((String) jwt.getClaims().get("sub"));
        if (userRepository.existsById(Long.parseLong(tokenDTO.getUserId())) && tokenDTO.getUserId().equals(Long.toString(id)))
            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        return new ResponseEntity<>(new TokenDTO(tokenDTO.getUserId(), null, null, tokenDTO.getRefreshToken()), HttpStatus.NOT_FOUND);
    }

    @Transactional
    public DeleteUserDTO deleteUser(User user, Long id) {
        if (user.getId() != null && user.getId().equals(id)) {
            userRepository.deleteById(id);
            return new DeleteUserDTO(true);
        }
        return new DeleteUserDTO(false);
    }


    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    public void setRefreshTokenAuthProvider(JwtAuthenticationProvider refreshTokenAuthProvider) {
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
    }

}
