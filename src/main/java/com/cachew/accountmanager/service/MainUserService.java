package com.cachew.accountmanager.service;

import com.cachew.accountmanager.dto.*;
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
            return new RegisterResponseDTO(false, "User with same username already exists", null);
        }

        userDetailsManager.createUser(user);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), Collections.EMPTY_LIST);

        return new RegisterResponseDTO(true, "user {" + user.getUsername() + "} is registered", tokenGenerator.createToken(authentication));
    }

    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
        Authentication authentication = null;
        try {
            authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (Exception e) {
            return new LoginResponseDTO(false, loginDTO.getUsername(), null);
        }
        return new LoginResponseDTO(true, loginDTO.getUsername(), tokenGenerator.createToken(authentication));
    }

    public ResponseEntity<TokenDTO> refreshToken(TokenDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();
        long id = Long.parseLong((String) jwt.getClaims().get("sub"));
        if (userRepository.existsById(Long.parseLong(tokenDTO.getUserId())) && tokenDTO.getUserId().equals(Long.toString(id)))
            return ResponseEntity.ok(tokenGenerator.createToken(authentication));
        return new ResponseEntity<>(new TokenDTO(null, null, null), HttpStatus.NOT_FOUND);
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
