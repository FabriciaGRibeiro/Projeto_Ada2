package com.example.securityapp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderCreationDTO {
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clientId;
    public OrderCreationDTO getClientId;

    public Long getClientId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

