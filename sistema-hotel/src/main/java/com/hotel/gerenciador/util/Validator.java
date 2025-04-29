package com.hotel.gerenciador.util;

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

    public static void validateNotFutureDate(LocalDate data) {
        if (data.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data não pode ser no futuro.");
        }
    }

    public static void validateNotFutureDateTime(LocalDateTime dateTime) {
        if (dateTime.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("A data e hora não podem ser no futuro.");
        }
    }

    public static void validateCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("O CPF precisa ter 11 dígitos.");
        }

        if (!isValidCpf(cpf)) {
            throw new IllegalArgumentException("O CPF fornecido é inválido.");
        }
    }

    private static boolean isValidCpf(String cpf) {
        int[] multiplicador1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] multiplicador2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (cpfLimpo.length() != 11) {
            return false;
        }

        String cpfCalculado = cpfLimpo.substring(0, 9);

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

        return cpfLimpo.equals(cpfCalculado);
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
        if (email == null || !Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            throw new IllegalArgumentException("O email fornecido é inválido.");
        }
    }

    public static String validateAndFormatEndereco(String endereco) {
        if (endereco == null || endereco.isBlank()) {
            throw new IllegalArgumentException("O endereço não pode ser nulo ou vazio.");
        }

        String regexEndereco = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s.,'-]+$";
        if (!Pattern.matches(regexEndereco, endereco)) {
            throw new IllegalArgumentException("O endereço contém caracteres inválidos.");
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

        return String.format("%s %s %s %s", logradouro, bairro, localidadeUf, cep);
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
        String regexBairro = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s.-]+$";
        if (!Pattern.matches(regexBairro, bairro)) {
            throw new IllegalArgumentException("O nome do bairro contém caracteres inválidos.");
        }
    }

    private static void validateLocalidadeUF(String localidadeUf) {
        if (localidadeUf == null || localidadeUf.isBlank()) {
            throw new IllegalArgumentException("A localidade/UF não pode ser nula ou vazia.");
        }

        String regexLocalidadeUf = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+/[A-Za-z]{2}$";
        if (!Pattern.matches(regexLocalidadeUf, localidadeUf)) {
            throw new IllegalArgumentException("A localidade e UF devem estar no formato: Localidade/UF");
        }
    }

    private static void validateCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new IllegalArgumentException("O CEP não pode ser nulo ou vazio.");
        }

        String regexCep = "^[0-9]{5}-?[0-9]{3}$";
        if (!Pattern.matches(regexCep, cep)) {
            throw new IllegalArgumentException("O CEP deve ter 8 dígitos. Exemplo: 12345-678");
        }
    }
}
