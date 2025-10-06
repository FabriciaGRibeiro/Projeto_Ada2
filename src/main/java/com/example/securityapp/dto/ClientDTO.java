package com.example.securityapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Número de documento é obrigatório")
    @Size(min = 11, max = 11, message = "Número de documento deve ter 11 caracteres (CPF)")
    @Pattern(regexp = "\\d+", message = "Número de documento deve conter apenas dígitos")
    private String documentNumber;

    private Long clientId;

    
    public Object getEmail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDocumentNumber() {
        return getDocumentNumber();
    }

    public Long getClientId() {
        return getClientId();
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
