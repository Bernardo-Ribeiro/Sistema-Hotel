package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ServicoTest {
    private Servico servico;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        servico = new Servico(
            1,
            "Lavanderia",
            "Serviço de lavagem e passagem de roupas",
            new BigDecimal("50.00"),
            true,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um serviço com dados válidos")
    void testCriarServicoComDadosValidos() {
        assertNotNull(servico);
        assertEquals(1, servico.getId());
        assertEquals("Lavanderia", servico.getNome());
        assertEquals("Serviço de lavagem e passagem de roupas", servico.getDescricao());
        assertEquals(new BigDecimal("50.00"), servico.getPreco());
        assertTrue(servico.isDisponivel());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir nome nulo")
    void testSetNomeNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setNome(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir nome vazio")
    void testSetNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setNome("   ");
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir descrição muito longa")
    void testSetDescricaoMuitoLonga() {
        String descricaoLonga = "a".repeat(256);
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setDescricao(descricaoLonga);
        });
    }

    @Test
    @DisplayName("Deve aceitar descrição nula")
    void testSetDescricaoNula() {
        servico.setDescricao(null);
        assertEquals("", servico.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir preço nulo")
    void testSetPrecoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setPreco(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir preço negativo")
    void testSetPrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setPreco(new BigDecimal("-10.00"));
        });
    }

    @Test
    @DisplayName("Deve atualizar disponibilidade do serviço")
    void testAtualizarDisponibilidade() {
        servico.setDisponivel(false);
        assertFalse(servico.isDisponivel());
        
        servico.setDisponivel(true);
        assertTrue(servico.isDisponivel());
    }

    @Test
    @DisplayName("Deve atualizar dados do serviço corretamente")
    void testAtualizarDadosServico() {
        String novoNome = "Lavanderia Express";
        String novaDescricao = "Serviço rápido de lavagem";
        BigDecimal novoPreco = new BigDecimal("75.00");
        
        servico.setNome(novoNome);
        servico.setDescricao(novaDescricao);
        servico.setPreco(novoPreco);
        
        assertEquals(novoNome, servico.getNome());
        assertEquals(novaDescricao, servico.getDescricao());
        assertEquals(novoPreco, servico.getPreco());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de criação futura")
    void testSetDataCriacaoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setDataCriacao(LocalDateTime.now().plusDays(1));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de atualização futura")
    void testSetDataAtualizacaoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            servico.setDataAtualizacao(LocalDateTime.now().plusDays(1));
        });
    }
} 