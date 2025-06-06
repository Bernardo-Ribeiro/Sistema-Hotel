package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConsumoServicosTest {
    private ConsumoServicos consumoServicos;
    private LocalDate dataConsumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataConsumo = LocalDate.now();
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        consumoServicos = new ConsumoServicos(
            1,
            101, // reservaId
            201, // servicoId
            2,   // quantidade
            dataConsumo,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um consumo de serviços com dados válidos")
    void testCriarConsumoServicosComDadosValidos() {
        assertNotNull(consumoServicos);
        assertEquals(1, consumoServicos.getId());
        assertEquals(101, consumoServicos.getReservaId());
        assertEquals(201, consumoServicos.getServicoId());
        assertEquals(2, consumoServicos.getQuantidade());
        assertEquals(dataConsumo, consumoServicos.getDataConsumo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir quantidade negativa")
    void testSetQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            consumoServicos.setQuantidade(-1);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir quantidade zero")
    void testSetQuantidadeZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            consumoServicos.setQuantidade(0);
        });
    }

    @Test
    @DisplayName("Deve atualizar ID da reserva corretamente")
    void testAtualizarReservaId() {
        int novaReservaId = 102;
        consumoServicos.setReservaId(novaReservaId);
        assertEquals(novaReservaId, consumoServicos.getReservaId());
    }

    @Test
    @DisplayName("Deve atualizar ID do serviço corretamente")
    void testAtualizarServicoId() {
        int novoServicoId = 202;
        consumoServicos.setServicoId(novoServicoId);
        assertEquals(novoServicoId, consumoServicos.getServicoId());
    }

    @Test
    @DisplayName("Deve atualizar quantidade corretamente")
    void testAtualizarQuantidade() {
        int novaQuantidade = 3;
        consumoServicos.setQuantidade(novaQuantidade);
        assertEquals(novaQuantidade, consumoServicos.getQuantidade());
    }

    @Test
    @DisplayName("Deve atualizar data de consumo corretamente")
    void testAtualizarDataConsumo() {
        LocalDate novaDataConsumo = LocalDate.now().plusDays(1);
        consumoServicos.setDataConsumo(novaDataConsumo);
        assertEquals(novaDataConsumo, consumoServicos.getDataConsumo());
    }

    @Test
    @DisplayName("Deve aceitar data de consumo nula")
    void testDataConsumoNula() {
        consumoServicos.setDataConsumo(null);
        assertNull(consumoServicos.getDataConsumo());
    }

    @Test
    @DisplayName("Deve atualizar ID corretamente")
    void testAtualizarId() {
        int novoId = 2;
        consumoServicos.setId(novoId);
        assertEquals(novoId, consumoServicos.getId());
    }
} 