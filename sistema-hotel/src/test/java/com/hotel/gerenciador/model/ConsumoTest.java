package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ConsumoTest {
    private Consumo consumo;
    private LocalDate dataConsumo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataConsumo = LocalDate.now();
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        consumo = new Consumo(
            1,
            1, // idReserva
            1, // idProduto
            25.50,
            2,
            dataConsumo,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um consumo com dados válidos")
    void testCriarConsumoComDadosValidos() {
        assertNotNull(consumo);
        assertEquals(1, consumo.getId());
        assertEquals(1, consumo.getIdReserva());
        assertEquals(1, consumo.getIdProduto());
        assertEquals(25.50, consumo.getValor());
        assertEquals(2, consumo.getQuantidade());
        assertEquals(dataConsumo, consumo.getDataConsumo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir valor negativo")
    void testSetValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            consumo.setValor(-10.00);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir quantidade negativa")
    void testSetQuantidadeNegativa() {
        assertThrows(IllegalArgumentException.class, () -> {
            consumo.setQuantidade(-1);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir quantidade zero")
    void testSetQuantidadeZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            consumo.setQuantidade(0);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de consumo futura")
    void testSetDataConsumoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            consumo.setDataConsumo(LocalDate.now().plusDays(1));
        });
    }

    @Test
    @DisplayName("Deve atualizar dados do consumo corretamente")
    void testAtualizarDadosConsumo() {
        double novoValor = 30.00;
        int novaQuantidade = 3;
        
        consumo.setValor(novoValor);
        consumo.setQuantidade(novaQuantidade);
        
        assertEquals(novoValor, consumo.getValor());
        assertEquals(novaQuantidade, consumo.getQuantidade());
    }

    @Test
    @DisplayName("Deve atualizar IDs corretamente")
    void testAtualizarIds() {
        int novaReservaId = 2;
        int novoProdutoId = 3;
        
        consumo.setIdReserva(novaReservaId);
        consumo.setIdProduto(novoProdutoId);
        
        assertEquals(novaReservaId, consumo.getIdReserva());
        assertEquals(novoProdutoId, consumo.getIdProduto());
    }
} 