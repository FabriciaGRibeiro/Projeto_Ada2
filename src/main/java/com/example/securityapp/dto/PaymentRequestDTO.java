package com.example.securityapp.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "O valor do pagamento é obrigatório")
    private BigDecimal amount;

    @NotBlank(message = "O método de pagamento é obrigatório")
    private String paymentMethod; 

    private String cardNumberToken;
    private String cardHolderName;
    private String cardExpirationMonth;
    private String cardExpirationYear;
    private String cardCvvToken; 
    

    // Para PIX
    private String pixKey;
    private String pixKeyType;

    private String bankAccountToken;
    private String bankName;

    public Object getPaymentMethod() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getAmount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
