package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class HospedeTest {
    private Hospede hospede;
    private LocalDate dataNascimento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataNascimento = LocalDate.of(1990, 1, 1);
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        hospede = new Hospede(
            1,
            "João Silva",
            "12345678900",
            "11999999999",
            "joao@email.com",
            "Rua das Flores, 123",
            dataNascimento,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um hóspede com dados válidos")
    void testCriarHospedeComDadosValidos() {
        assertNotNull(hospede);
        assertEquals(1, hospede.getId());
        assertEquals("João Silva", hospede.getNome());
        assertEquals("12345678900", hospede.getCpf());
        assertEquals("11999999999", hospede.getTelefone());
        assertEquals("joao@email.com", hospede.getEmail());
        assertEquals("Rua das Flores, 123", hospede.getEndereco());
        assertEquals(dataNascimento, hospede.getDataNascimento());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir CPF inválido")
    void testSetCpfInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            hospede.setCpf("123"); // CPF inválido
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir email inválido")
    void testSetEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            hospede.setEmail("emailinvalido"); // Email inválido
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir telefone inválido")
    void testSetTelefoneInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            hospede.setTelefone("123"); // Telefone inválido
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de nascimento futura")
    void testSetDataNascimentoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            hospede.setDataNascimento(LocalDate.now().plusDays(1));
        });
    }

    @Test
    @DisplayName("Deve atualizar os dados do hóspede corretamente")
    void testAtualizarDadosHospede() {
        String novoNome = "Maria Silva";
        String novoTelefone = "11988888888";
        
        hospede.setNome(novoNome);
        hospede.setTelefone(novoTelefone);
        
        assertEquals(novoNome, hospede.getNome());
        assertEquals(novoTelefone, hospede.getTelefone());
    }
} 