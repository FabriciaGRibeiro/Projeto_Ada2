package com.example.securityapp.controller;

import com.example.securityapp.dto.ClientDTO;
import com.example.securityapp.dto.UserRegistrationDTO;
import com.example.securityapp.model.User;
import com.example.securityapp.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final UserService userService;

    public ClientController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Apenas administradores podem cadastrar novos clientes
    public ResponseEntity<?> registerClient(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            // Reutiliza o método de registo de utilizador, pois o cliente é um utilizador
            User newClient = userService.registerNewUser(registrationDTO);
            userService.sendVerificationEmail(newClient); // Simula o envio de e-mail
            return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao registar cliente: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')") // Administradores e utilizadores podem listar clientes
    public ResponseEntity<List<User>> getAllClients() {
        List<User> clients = userService.findAllUsers();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)") // Admin ou o próprio
                                                                                                  // utilizador
    public ResponseEntity<User> getClientById(@PathVariable Long id) {
        try {
            User client = userService.findUserById(id);
            return ResponseEntity.ok(client);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == authentication.principal.id)") // Admin ou o próprio
                                                                                                  // utilizador
    public ResponseEntity<?> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
        try {
            User updatedClient = userService.updateUser(id, clientDTO);
            return ResponseEntity.ok(updatedClient);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

}
