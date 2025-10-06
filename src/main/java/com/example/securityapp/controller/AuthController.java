package com.example.securityapp.controller;

import com.example.securityapp.dto.LoginRequestDTO;
import com.example.securityapp.dto.UserRegistrationDTO;
import com.example.securityapp.model.User;
import com.example.securityapp.security.JwtTokenProvider;
import com.example.securityapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User newUser = userService.registerNewUser(registrationDTO);
            userService.sendVerificationEmail(newUser); // Simula o envio de e-mail
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilizador registado com sucesso. Verifique o seu e-mail para ativação.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao registar utilizador: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Gerar token JWT
        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(token);
    }

    // Endpoint de verificação de e-mail (a ser implementado com token real)
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        // Aqui a lógica real de verificação de e-mail seria implementada
        // Por enquanto, apenas uma simulação
        if (userService.verifyEmail(token)) {
            return ResponseEntity.ok("E-mail verificado com sucesso!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token de verificação inválido ou expirado.");
        }
    }
}

