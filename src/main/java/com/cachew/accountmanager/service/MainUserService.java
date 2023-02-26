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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class MainUserService {

    private UserManager userDetailsManager;

    private TokenGenerator tokenGenerator;

    private DaoAuthenticationProvider daoAuthenticationProvider;

    private JwtAuthenticationProvider refreshTokenAuthProvider;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public MainUserService(UserManager userManager,
                           TokenGenerator tokenGenerator,
                           DaoAuthenticationProvider daoAuthenticationProvider,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userManager;
        this.tokenGenerator = tokenGenerator;
        this.daoAuthenticationProvider = daoAuthenticationProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<String> createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            StatusDTO status = new StatusDTO();
            status.setCode(409);
            status.setDescription("User already exists");
            return new ResponseEntity<>(status.toJson(), HttpStatus.CONFLICT);
        }

        userDetailsManager.createUser(user);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(user, user.getPassword(), Collections.EMPTY_LIST);

        RegisterResponseDTO response = new RegisterResponseDTO(String.valueOf(user.getId()), user.getUsername(), tokenGenerator.createToken(authentication));

        return new ResponseEntity<>(response.toJson(), HttpStatus.OK);
    }

    public ResponseEntity<String> loginUser(LoginRequestDTO loginDTO) {
        Authentication authentication;
        try {
            authentication = daoAuthenticationProvider.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(loginDTO.getUsername(), loginDTO.getPassword()));
        } catch (Exception e) {
            return new ResponseEntity<>(new StatusDTO(403, "Unauthorized").toJson(), HttpStatus.UNAUTHORIZED);
        }
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        return new ResponseEntity<>(new LoginResponseDTO(String.valueOf(details.getId()), details.getUsername(), tokenGenerator.createToken(authentication)).toJson(), HttpStatus.OK);
    }

    public ResponseEntity<String> refreshToken(TokenRefreshDTO tokenDTO) {
        Authentication authentication = refreshTokenAuthProvider.authenticate(new BearerTokenAuthenticationToken(tokenDTO.getRefreshToken()));
        Jwt jwt = (Jwt) authentication.getCredentials();
        long id = Long.parseLong((String) jwt.getClaims().get("sub"));
        if (userRepository.existsById(Long.parseLong(tokenDTO.getUserId())) && tokenDTO.getUserId().equals(Long.toString(id)))
            return ResponseEntity.ok(tokenGenerator.createToken(authentication).toJson());
        return new ResponseEntity<>(new StatusDTO(404, "user not found").toJson(), HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<String> deleteUser(User user, String password) {
        if (user == null)
            return new ResponseEntity<>(new StatusDTO(404, "user not found").toJson(), HttpStatus.NOT_FOUND);
        Optional<User> userRepo = userRepository.findByUsername(user.getUsername());
        if (userRepo.isPresent()) {
            User userRepoGet = userRepo.get();
            if (userRepoGet.getPassword().equals(passwordEncoder.encode(password))) {
                userRepository.deleteById(userRepoGet.getId());
                return ResponseEntity.ok(new DeleteResponseDTO(true).toJson());
            }
        }
        return new ResponseEntity<>(new StatusDTO(401, "unauthorized").toJson(), HttpStatus.UNAUTHORIZED);
    }


    @Autowired
    @Qualifier("jwtRefreshTokenAuthProvider")
    public void setRefreshTokenAuthProvider(JwtAuthenticationProvider refreshTokenAuthProvider) {
        this.refreshTokenAuthProvider = refreshTokenAuthProvider;
    }

}
