package com.hotel.gerenciador.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Validator {

    public static void validateTelefone(String telefone) {
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("O telefone não pode ser nulo ou vazio.");
        }

        String regex = "^(\\(\\d{2}\\)\\s?)?(\\d{4,5}-?\\d{4})$|^\\d{10,11}$";
        if (!Pattern.matches(regex, telefone)) {
            throw new IllegalArgumentException("O telefone fornecido é inválido. Ex: (11)98765-4321");
        }
    }

    public static void validatePositiveValue(double valor) {
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
    }

    public static void validatePositiveValue(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
    }

    public static void validateNotFutureDate(LocalDate data) {
        if (data != null && data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data não pode ser no futuro.");
        }
    }

    public static void validateNotFutureDateTime(LocalDateTime dateTime) {
        if (dateTime != null && dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora não podem ser no futuro.");
        }
    }

    public static void validateCpf(String cpf) {
        System.out.println("Validator.validateCpf: Recebido cpf = [" + cpf + "]");
        if (cpf == null) {
            throw new IllegalArgumentException("O CPF não pode ser nulo.");
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");
        System.out.println("Validator.validateCpf: cpfLimpo = [" + cpfLimpo + "], length = " + cpfLimpo.length());

        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("O CPF precisa ter 11 dígitos numéricos. (Valor processado: " + cpfLimpo + ")");
        }

        if (!isValidCpfInternal(cpfLimpo)) {
            System.err.println("Validator.validateCpf: isValidCpfInternal retornou false para cpfLimpo = [" + cpfLimpo + "]");
            throw new IllegalArgumentException("O CPF fornecido é inválido (dígitos verificadores não conferem).");
        }
        System.out.println("Validator.validateCpf: CPF [" + cpfLimpo + "] validado com sucesso.");
    }

    private static boolean isValidCpfInternal(String cpfLimpoDe11Digitos) {
        if (cpfLimpoDe11Digitos.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] multiplicador1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] multiplicador2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        String cpfCalculado = cpfLimpoDe11Digitos.substring(0, 9);
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Integer.parseInt(String.valueOf(cpfCalculado.charAt(i))) * multiplicador1[i];
        }

        int resto = soma % 11;
        int digito1 = resto < 2 ? 0 : 11 - resto;
        cpfCalculado += digito1;

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Integer.parseInt(String.valueOf(cpfCalculado.charAt(i))) * multiplicador2[i];
        }

        resto = soma % 11;
        int digito2 = resto < 2 ? 0 : 11 - resto;
        cpfCalculado += digito2;

        return cpfLimpoDe11Digitos.equals(cpfCalculado); 
    }

    public static void validatePassword(String senha) {
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres.");
        }
        
        if (!senha.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException("A senha deve conter pelo menos uma letra maiúscula, uma minúscula, um número e um caractere especial.");
        }
    }

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O email não pode ser nulo ou vazio.");
        }
        if (!Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email)) {
            throw new IllegalArgumentException("O email fornecido é inválido.");
        }
    }

    public static void validateEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("O endereço não pode ser nulo ou vazio.");
        }

        String[] enderecoParts = endereco.split(",");
    
        if (enderecoParts.length != 4) {
            throw new IllegalArgumentException("O endereço deve ter o formato: Logradouro/Nome, Bairro/Distrito, Localidade/UF, CEP (Exemplo: Rua ABC, Centro, São Paulo/SP, 12345-678)");
        }
    
        String logradouro = enderecoParts[0].trim();
        String bairro = enderecoParts[1].trim();
        String localidadeUf = enderecoParts[2].trim();
        String cep = enderecoParts[3].trim();
    
        validateLogradouro(logradouro);
        validateBairro(bairro);
        validateLocalidadeUF(localidadeUf);
        validateCep(cep);
    }
    
    private static void validateLogradouro(String logradouro) {
        if (logradouro == null || logradouro.isBlank()) {
            throw new IllegalArgumentException("O logradouro não pode ser nulo ou vazio.");
        }
    }

    private static void validateBairro(String bairro) {
        if (bairro == null || bairro.isBlank()) {
            throw new IllegalArgumentException("O bairro não pode ser nulo ou vazio.");
        }
        String regexBairro = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s.'-]+$";
        if (!Pattern.matches(regexBairro, bairro)) {
            throw new IllegalArgumentException("O nome do bairro contém caracteres inválidos.");
        }
    }

    private static void validateLocalidadeUF(String localidadeUf) {
        if (localidadeUf == null || localidadeUf.isBlank()) {
            throw new IllegalArgumentException("A localidade/UF não pode ser nula ou vazia.");
        }

        String regexLocalidadeUf = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s.'-]+/[A-Za-z]{2}$";
        if (!Pattern.matches(regexLocalidadeUf, localidadeUf)) {
            throw new IllegalArgumentException("A localidade e UF devem estar no formato: Localidade/UF (Ex: São Paulo/SP)");
        }
    }

    private static void validateCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("O CEP não pode ser nulo ou vazio.");
        }

        String regexCep = "^[0-9]{5}-?[0-9]{3}$";
        if (!Pattern.matches(regexCep, cep)) {
            throw new IllegalArgumentException("O CEP deve ter 8 dígitos e pode estar no formato 12345-678 ou 12345678.");
        }
    }
    
    public static void validateDateRange(LocalDate checkIn, LocalDate checkOut) {
        if (checkIn == null || checkOut == null) {
            throw new IllegalArgumentException("As datas de check-in e check-out não podem ser nulas.");
        }
        
        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("A data de check-out deve ser igual ou após a data de check-in.");
        }
        
        if (checkIn.isEqual(checkOut)) {
            throw new IllegalArgumentException("O período da reserva deve ser de pelo menos um dia (data de check-out diferente da data de check-in).");
        }
    }

    public static void validateDisponibilidade(boolean disponivel) {
        if (!disponivel) {
            throw new IllegalStateException("O quarto não está disponível para o período solicitado.");
        }
    }
}