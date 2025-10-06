package com.example.securityapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String documentNumber; // e.g., CPF

    @Column(nullable = false)
    private String password;

    private boolean emailVerified = false;

    // Getters e Setters são gerados pelo Lombok (@Data)

    public void setEmail(Object email) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setDocumentNumber(Object documentNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPassword(String encode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEmailVerified(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getEmail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getDocumentNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

