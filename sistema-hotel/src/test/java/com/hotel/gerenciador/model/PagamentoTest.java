package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusPagamento;

public class PagamentoTest {
    private Pagamento pagamento;
    private LocalDate dataPagamento;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataPagamento = LocalDate.now();
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        pagamento = new Pagamento(
            1,
            new BigDecimal("500.00"),
            dataPagamento,
            MetodoPagamento.CARTAO_CREDITO,
            StatusPagamento.PENDENTE,
            1,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um pagamento com dados válidos")
    void testCriarPagamentoComDadosValidos() {
        assertNotNull(pagamento);
        assertEquals(1, pagamento.getId());
        assertEquals(new BigDecimal("500.00"), pagamento.getValor());
        assertEquals(dataPagamento, pagamento.getDataPagamento());
        assertEquals(MetodoPagamento.CARTAO_CREDITO, pagamento.getMetodo());
        assertEquals(StatusPagamento.PENDENTE, pagamento.getStatus());
        assertEquals(1, pagamento.getReservaId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir valor nulo")
    void testSetValorNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setValor(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir valor zero")
    void testSetValorZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setValor(BigDecimal.ZERO);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir valor negativo")
    void testSetValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setValor(new BigDecimal("-100.00"));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de pagamento futura")
    void testSetDataPagamentoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            pagamento.setDataPagamento(LocalDate.now().plusDays(1));
        });
    }

    @Test
    @DisplayName("Deve atualizar status do pagamento corretamente")
    void testAtualizarStatusPagamento() {
        pagamento.setStatus(StatusPagamento.PAGO);
        assertEquals(StatusPagamento.PAGO, pagamento.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar método de pagamento corretamente")
    void testAtualizarMetodoPagamento() {
        pagamento.setMetodo(MetodoPagamento.PIX);
        assertEquals(MetodoPagamento.PIX, pagamento.getMetodo());
    }
} 