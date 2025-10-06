package com.example.securityapp.util;

public class DocumentValidator {

    public static boolean isValidCPF(String cpf) {
        if (cpf == null || object.length() != 11 || object.matches("(\\d)\\1{10}")) return false;

        char dig10, dig11;
        int sm, i, r, num, peso;

        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 10;
            for (i = 0; i < 9; i++) {
                num = (int) (object.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig10 = '0';
            else dig10 = (char) (r + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 11;
            for (i = 0; i < 10; i++) {
                num = (int) (object.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) dig11 = '0';
            else dig11 = (char) (r + 48);

            // Verifica se os digitos calculados conferem com os digitos informados.
            return (dig10 == object.charAt(9)) && (dig11 == object.charAt(10));
        } catch (Exception erro) {
            return false;
        }
    }
}

