package com.hotel.gerenciador.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValidatorTest {

    @Test
    @DisplayName("Deve validar telefone corretamente")
    void testValidateTelefone() {
        // Telefones válidos
        assertDoesNotThrow(() -> Validator.validateTelefone("(11)98765-4321"));
        assertDoesNotThrow(() -> Validator.validateTelefone("(11)8765-4321"));
        assertDoesNotThrow(() -> Validator.validateTelefone("11987654321"));
        
        // Telefones inválidos
        assertThrows(IllegalArgumentException.class, () -> Validator.validateTelefone(null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateTelefone(""));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateTelefone("123"));
    }

    @Test
    @DisplayName("Deve validar valores positivos")
    void testValidatePositiveValue() {
        // Valores válidos
        assertDoesNotThrow(() -> Validator.validatePositiveValue(1.0));
        assertDoesNotThrow(() -> Validator.validatePositiveValue(new BigDecimal("1.0")));
        
        // Valores inválidos
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePositiveValue(0.0));
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePositiveValue(-1.0));
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePositiveValue(new BigDecimal("0")));
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePositiveValue(null));
    }

    @Test
    @DisplayName("Deve validar datas não futuras")
    void testValidateNotFutureDate() {
        // Datas válidas
        assertDoesNotThrow(() -> Validator.validateNotFutureDate(LocalDate.now()));
        assertDoesNotThrow(() -> Validator.validateNotFutureDate(LocalDate.now().minusDays(1)));
        
        // Datas inválidas
        assertThrows(IllegalArgumentException.class, () -> 
            Validator.validateNotFutureDate(LocalDate.now().plusDays(1)));
    }

    @Test
    @DisplayName("Deve validar CPF corretamente")
    void testValidateCpf() {
        // CPFs válidos
        assertDoesNotThrow(() -> Validator.validateCpf("529.982.247-25"));
        assertDoesNotThrow(() -> Validator.validateCpf("52998224725"));
        
        // CPFs inválidos
        assertThrows(IllegalArgumentException.class, () -> Validator.validateCpf(null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateCpf("123"));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateCpf("111.111.111-11"));
    }

    @Test
    @DisplayName("Deve validar senha corretamente")
    void testValidatePassword() {
        // Senhas válidas
        assertDoesNotThrow(() -> Validator.validatePassword("Senha@123"));
        assertDoesNotThrow(() -> Validator.validatePassword("Abc@123456"));
        
        // Senhas inválidas
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePassword(null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePassword("1234567"));
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePassword("senha123"));
        assertThrows(IllegalArgumentException.class, () -> Validator.validatePassword("SENHA123"));
    }

    @Test
    @DisplayName("Deve validar email corretamente")
    void testValidateEmail() {
        // Emails válidos
        assertDoesNotThrow(() -> Validator.validateEmail("usuario@email.com"));
        assertDoesNotThrow(() -> Validator.validateEmail("usuario.nome@dominio.com.br"));
        
        // Emails inválidos
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEmail(null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEmail(""));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEmail("email.invalido"));
    }

    @Test
    @DisplayName("Deve validar endereço corretamente")
    void testValidateEndereco() {
        // Endereços válidos
        assertDoesNotThrow(() -> Validator.validateEndereco("Rua das Flores, Centro, São Paulo/SP, 12345-678"));
        assertDoesNotThrow(() -> Validator.validateEndereco("Av. Paulista, Bela Vista, São Paulo/SP, 12345678"));
        
        // Endereços inválidos
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEndereco(null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEndereco(""));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEndereco("Rua das Flores"));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateEndereco("Rua das Flores, Centro, São Paulo, 12345-678"));
    }

    @Test
    @DisplayName("Deve validar intervalo de datas corretamente")
    void testValidateDateRange() {
        LocalDate hoje = LocalDate.now();
        LocalDate amanha = hoje.plusDays(1);
        LocalDate depoisAmanha = hoje.plusDays(2);
        
        // Intervalos válidos
        assertDoesNotThrow(() -> Validator.validateDateRange(hoje, amanha));
        assertDoesNotThrow(() -> Validator.validateDateRange(hoje, depoisAmanha));
        
        // Intervalos inválidos
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDateRange(null, amanha));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDateRange(hoje, null));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDateRange(amanha, hoje));
        assertThrows(IllegalArgumentException.class, () -> Validator.validateDateRange(hoje, hoje));
    }

    @Test
    @DisplayName("Deve validar disponibilidade corretamente")
    void testValidateDisponibilidade() {
        // Disponibilidade válida
        assertDoesNotThrow(() -> Validator.validateDisponibilidade(true));
        
        // Disponibilidade inválida
        assertThrows(IllegalStateException.class, () -> Validator.validateDisponibilidade(false));
    }
} 