package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.TipoQuarto;

public class QuartoTest {
    private Quarto quarto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        quarto = new Quarto(
            1,
            101,
            TipoQuarto.SOLTEIRO,
            new BigDecimal("200.00"),
            StatusQuarto.DISPONIVEL,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um quarto com dados válidos")
    void testCriarQuartoComDadosValidos() {
        assertNotNull(quarto);
        assertEquals(1, quarto.getId());
        assertEquals(101, quarto.getNumeroQuarto());
        assertEquals(TipoQuarto.SOLTEIRO, quarto.getTipo());
        assertEquals(new BigDecimal("200.00"), quarto.getPrecoDiaria());
        assertEquals(StatusQuarto.DISPONIVEL, quarto.getStatus());
    }

    @Test
    @DisplayName("Deve aceitar todos os tipos de quarto válidos")
    void testTiposDeQuartoValidos() {
        // Testa todos os tipos de quarto disponíveis
        quarto.setTipo(TipoQuarto.SOLTEIRO);
        assertEquals(TipoQuarto.SOLTEIRO, quarto.getTipo());

        quarto.setTipo(TipoQuarto.CASAL);
        assertEquals(TipoQuarto.CASAL, quarto.getTipo());

        quarto.setTipo(TipoQuarto.FAMILIA);
        assertEquals(TipoQuarto.FAMILIA, quarto.getTipo());

        quarto.setTipo(TipoQuarto.LUXO);
        assertEquals(TipoQuarto.LUXO, quarto.getTipo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir número de quarto inválido")
    void testSetNumeroQuartoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            quarto.setNumeroQuarto(0);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir tipo de quarto nulo")
    void testSetTipoQuartoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            quarto.setTipo(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir preço diária negativo")
    void testSetPrecoDiariaNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            quarto.setPrecoDiaria(new BigDecimal("-100.00"));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir status nulo")
    void testSetStatusNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            quarto.setStatus(null);
        });
    }

    @Test
    @DisplayName("Deve calcular preço total corretamente")
    void testCalcularPrecoTotal() {
        BigDecimal precoTotal = quarto.calcularPrecoTotal(3);
        assertEquals(new BigDecimal("600.00"), precoTotal);
    }

    @Test
    @DisplayName("Deve lançar exceção ao calcular preço com dias negativos")
    void testCalcularPrecoTotalDiasNegativos() {
        assertThrows(IllegalArgumentException.class, () -> {
            quarto.calcularPrecoTotal(-1);
        });
    }

    @Test
    @DisplayName("Deve verificar disponibilidade do quarto")
    void testIsDisponivel() {
        assertTrue(quarto.isDisponivel());
        quarto.setStatus(StatusQuarto.OCUPADO);
        assertFalse(quarto.isDisponivel());
    }
} 