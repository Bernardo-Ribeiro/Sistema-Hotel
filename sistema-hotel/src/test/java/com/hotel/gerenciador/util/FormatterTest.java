package com.hotel.gerenciador.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FormatterTest {

    @Test
    @DisplayName("Deve formatar data corretamente")
    void testFormatDate() {
        LocalDate data = LocalDate.of(2024, 3, 15);
        assertEquals("15/03/2024", Formatter.formatDate(data));
        assertEquals("", Formatter.formatDate(null));
    }

    @Test
    @DisplayName("Deve formatar data e hora corretamente")
    void testFormatDateTime() {
        LocalDateTime dataHora = LocalDateTime.of(2024, 3, 15, 14, 30, 45);
        assertEquals("15/03/2024 14:30:45", Formatter.formatDateTime(dataHora));
        assertEquals("", Formatter.formatDateTime(null));
    }

    @Test
    @DisplayName("Deve formatar moeda corretamente")
    void testFormatCurrency() {
        // Teste com double
        assertEquals("R$ 1.234,56", Formatter.formatCurrency(1234.56));
        assertEquals("R$ 0,00", Formatter.formatCurrency(0.0));
        assertEquals("R$ -1.234,56", Formatter.formatCurrency(-1234.56));

        // Teste com BigDecimal
        assertEquals("R$ 1.234,56", Formatter.formatCurrency(new BigDecimal("1234.56")));
        assertEquals("R$ 0,00", Formatter.formatCurrency(new BigDecimal("0.00")));
        assertEquals("R$ 0,00", Formatter.formatCurrency(null));
    }

    @Test
    @DisplayName("Deve formatar decimal corretamente")
    void testFormatDecimal() {
        // Teste com double
        assertEquals("1.234,56", Formatter.formatDecimal(1234.56, 2));
        assertEquals("1.234", Formatter.formatDecimal(1234.56, 0));
        assertEquals("1.234,560", Formatter.formatDecimal(1234.56, 3));

        // Teste com BigDecimal
        assertEquals("1.234,56", Formatter.formatDecimal(new BigDecimal("1234.56"), 2));
        assertEquals("1.234", Formatter.formatDecimal(new BigDecimal("1234.56"), 0));
        assertEquals("1.234,560", Formatter.formatDecimal(new BigDecimal("1234.56"), 3));
        assertEquals("", Formatter.formatDecimal(null, 2));
    }

    @Test
    @DisplayName("Deve formatar telefone corretamente")
    void testFormatPhone() {
        // Telefones com 11 dígitos
        assertEquals("(11) 98765-4321", Formatter.formatPhone("11987654321"));
        assertEquals("(11) 98765-4321", Formatter.formatPhone("(11)98765-4321"));
        
        // Telefones com 10 dígitos
        assertEquals("(11) 8765-4321", Formatter.formatPhone("1187654321"));
        assertEquals("(11) 8765-4321", Formatter.formatPhone("(11)8765-4321"));
        
        // Casos especiais
        assertEquals("", Formatter.formatPhone(null));
        assertEquals("", Formatter.formatPhone(""));
        assertEquals("123", Formatter.formatPhone("123")); // Número inválido retorna como está
    }

    @Test
    @DisplayName("Deve formatar email corretamente")
    void testFormatEmail() {
        assertEquals("usuario@email.com", Formatter.formatEmail("Usuario@Email.com"));
        assertEquals("usuario@email.com", Formatter.formatEmail(" usuario@email.com "));
        assertEquals("", Formatter.formatEmail(null));
        assertEquals("", Formatter.formatEmail(""));
    }

    @Test
    @DisplayName("Deve formatar CPF corretamente")
    void testFormatCpf() {
        // CPF válido
        assertEquals("123.456.789-09", Formatter.formatCpf("12345678909"));
        
        // Casos especiais
        assertEquals("", Formatter.formatCpf(null));
        assertEquals("123", Formatter.formatCpf("123")); // CPF inválido retorna como está
        assertEquals("123.456.789-09", Formatter.formatCpf("123.456.789-09")); // Já formatado
    }
} 