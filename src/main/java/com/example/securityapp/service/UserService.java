package com.example.securityapp.service;

import com.example.securityapp.dto.ClientDTO;
import com.example.securityapp.dto.UserRegistrationDTO;
import com.example.securityapp.model.User;
import com.example.securityapp.repository.UserRepository;
import com.example.securityapp.util.DocumentValidator;
import com.example.securityapp.util.EmailValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(UserRegistrationDTO registrationDTO) {
        // 1. Validar formato do e-mail
        if (!EmailValidator.isValid(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }

        // 2. Validar formato do documento (CPF)
        if (!DocumentValidator.isValidCPF(registrationDTO.getDocumentNumber())) {
            throw new IllegalArgumentException("Número de documento (CPF) inválido.");
        }

        // 3. Verificar duplicidade de e-mail
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new IllegalArgumentException("Já existe um utilizador registado com este e-mail.");
        }

        // 4. Verificar duplicidade de documento
        if (userRepository.existsByDocumentNumber(registrationDTO.getDocumentNumber())) {
            throw new IllegalArgumentException("Já existe um utilizador registado com este número de documento.");
        }

        // 5. Criar novo utilizador
        User user = new User();
        user.setEmail(registrationDTO.getEmail());
        user.setDocumentNumber(registrationDTO.getDocumentNumber());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword())); // Criptografar a senha
        user.setEmailVerified(false); // E-mail ainda não verificado

        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado com ID: " + id));
    }

    public java.util.List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User updateUser(Long id, ClientDTO clientDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilizador não encontrado com ID: " + id));

        // Validar formato do e-mail se alterado
        if (!existingUser.getEmail().equals(clientDTO.getEmail()) && !EmailValidator.isValid(clientDTO.getEmail())) {
            throw new IllegalArgumentException("Formato de e-mail inválido.");
        }
        // Verificar duplicidade de e-mail se alterado
        if (!existingUser.getEmail().equals(clientDTO.getEmail()) && userRepository.existsByEmail(clientDTO.getEmail())) {
            throw new IllegalArgumentException("Já existe um utilizador registado com este e-mail.");
        }

        // Validar formato do documento se alterado
        if (!existingUser.getDocumentNumber().equals(clientDTO.getDocumentNumber()) && !DocumentValidator.isValidCPF(clientDTO.getDocumentNumber())) {
            throw new IllegalArgumentException("Número de documento (CPF) inválido.");
        }
        // Verificar duplicidade de documento se alterado
        if (!existingUser.getDocumentNumber().equals(clientDTO.getDocumentNumber()) && userRepository.existsByDocumentNumber(clientDTO.getDocumentNumber())) {
            throw new IllegalArgumentException("Já existe um utilizador registado com este número de documento.");
        }

        existingUser.setName(clientDTO.getName());
        existingUser.setEmail(clientDTO.getEmail());
        existingUser.setDocumentNumber(clientDTO.getDocumentNumber());
        // Senha não é atualizada por este método, deve ter um método separado para isso

        return userRepository.save(existingUser);
    }

    // Método para simular o envio de e-mail de verificação (a ser implementado)
    public void sendVerificationEmail(User user) {
        // Lógica para gerar um token de verificação e enviar e-mail
        System.out.println("E-mail de verificação enviado para: " + user.getEmail());
    }

    // Método para simular a verificação de e-mail (a ser implementado)
    public boolean verifyEmail(String token) {
        // Lógica para validar o token e marcar o e-mail como verificado
        System.out.println("E-mail verificado com sucesso!");
        return true; // Simulação
    }
}

