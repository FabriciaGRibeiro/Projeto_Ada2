package com.example.securityapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Número de documento é obrigatório")
    @Size(min = 11, max = 11, message = "Número de documento deve ter 11 caracteres (CPF)")
    @Pattern(regexp = "\\d+", message = "Número de documento deve conter apenas dígitos")
    private String documentNumber;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{6,}$",
             message = "Senha deve conter pelo menos 6 caracteres, incluindo letras maiúsculas, minúsculas, números e caracteres especiais")
    private String password;

    public String getEmail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDocumentNumber() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public CharSequence getPassword() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

