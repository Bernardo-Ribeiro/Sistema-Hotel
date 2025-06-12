package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.TipoQuarto;
import com.hotel.gerenciador.util.StatusQuarto;

public class ReservaTest {
    private Reserva reserva;
    private Hospede hospede;
    private Quarto quarto;
    private LocalDate dataCheckIn;
    private LocalDate dataCheckOut;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        dataCheckIn = LocalDate.now().plusDays(1);
        dataCheckOut = LocalDate.now().plusDays(3);

        hospede = new Hospede(
            1,
            "João Silva",
            "12345678900",
            "11999999999",
            "joao@email.com",
            "Rua das Flores, 123",
            LocalDate.of(1990, 1, 1),
            dataCriacao,
            dataAtualizacao
        );

        quarto = new Quarto(
            1,
            101,
            TipoQuarto.SOLTEIRO,
            new BigDecimal("200.00"),
            StatusQuarto.DISPONIVEL,
            dataCriacao,
            dataAtualizacao
        );

        reserva = new Reserva(
            1,
            hospede,
            quarto,
            dataCheckIn,
            dataCheckOut,
            StatusReserva.CONFIRMADA,
            null,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar uma reserva com dados válidos")
    void testCriarReservaComDadosValidos() {
        assertNotNull(reserva);
        assertEquals(1, reserva.getId());
        assertEquals(hospede, reserva.getHospede());
        assertEquals(quarto, reserva.getQuarto());
        assertEquals(dataCheckIn, reserva.getDataCheckIn());
        assertEquals(dataCheckOut, reserva.getDataCheckOut());
        assertEquals(StatusReserva.CONFIRMADA, reserva.getStatus());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir hóspede nulo")
    void testSetHospedeNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.setHospede(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir quarto nulo")
    void testSetQuartoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.setQuarto(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de check-in nula")
    void testSetDataCheckInNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.setDataCheckIn(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de check-out nula")
    void testSetDataCheckOutNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.setDataCheckOut(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir check-in posterior ao check-out")
    void testSetDataCheckInPosteriorAoCheckOut() {
        LocalDate checkInPosterior = dataCheckOut.plusDays(1);
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.setDataCheckIn(checkInPosterior);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir check-out anterior ao check-in")
    void testSetDataCheckOutAnteriorAoCheckIn() {
        LocalDate checkOutAnterior = dataCheckIn.minusDays(1);
        assertThrows(IllegalArgumentException.class, () -> {
            reserva.setDataCheckOut(checkOutAnterior);
        });
    }

    @Test
    @DisplayName("Deve calcular valor total corretamente")
    void testCalcularValorTotal() {
        BigDecimal valorTotal = reserva.calcularValorTotal();
        assertEquals(new BigDecimal("400.00"), valorTotal); // 2 dias * R$200,00
    }

    @Test
    @DisplayName("Deve verificar se reserva está ativa")
    void testIsAtiva() {
        assertTrue(reserva.isAtiva());
        reserva.setStatus(StatusReserva.CANCELADA);
        assertFalse(reserva.isAtiva());
    }

    @Test
    @DisplayName("Deve atualizar valor total ao alterar datas")
    void testAtualizarValorTotalAoAlterarDatas() {
        LocalDate novoCheckOut = dataCheckIn.plusDays(4);
        reserva.setDataCheckOut(novoCheckOut);
        assertEquals(new BigDecimal("800.00"), reserva.getValorTotal()); // 4 dias * R$200,00
    }
} 