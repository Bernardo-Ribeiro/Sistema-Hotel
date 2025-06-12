package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import com.hotel.gerenciador.util.StatusManutencao;

public class ManutencaoTest {
    private Manutencao manutencao;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    @BeforeEach
    void setUp() {
        dataInicio = LocalDate.now();
        dataFim = LocalDate.now().plusDays(3);
        
        manutencao = new Manutencao(
            101, // idQuarto
            dataInicio,
            dataFim,
            "Manutenção do ar condicionado",
            StatusManutencao.PENDENTE,
            1 // idFuncionario
        );
    }

    @Test
    @DisplayName("Deve criar uma manutenção com dados válidos")
    void testCriarManutencaoComDadosValidos() {
        assertNotNull(manutencao);
        assertEquals(101, manutencao.getIdQuarto());
        assertEquals(dataInicio, manutencao.getDataInicio());
        assertEquals(dataFim, manutencao.getDataFim());
        assertEquals("Manutenção do ar condicionado", manutencao.getDescricao());
        assertEquals(StatusManutencao.PENDENTE, manutencao.getStatus());
        assertEquals(1, manutencao.getIdFuncionario());
    }

    @Test
    @DisplayName("Deve atualizar ID do quarto corretamente")
    void testAtualizarIdQuarto() {
        Integer novoIdQuarto = 102;
        manutencao.setIdQuarto(novoIdQuarto);
        assertEquals(novoIdQuarto, manutencao.getIdQuarto());
    }

    @Test
    @DisplayName("Deve atualizar datas corretamente")
    void testAtualizarDatas() {
        LocalDate novaDataInicio = LocalDate.now().plusDays(1);
        LocalDate novaDataFim = LocalDate.now().plusDays(5);
        
        manutencao.setDataInicio(novaDataInicio);
        manutencao.setDataFim(novaDataFim);
        
        assertEquals(novaDataInicio, manutencao.getDataInicio());
        assertEquals(novaDataFim, manutencao.getDataFim());
    }

    @Test
    @DisplayName("Deve atualizar descrição corretamente")
    void testAtualizarDescricao() {
        String novaDescricao = "Manutenção do sistema hidráulico";
        manutencao.setDescricao(novaDescricao);
        assertEquals(novaDescricao, manutencao.getDescricao());
    }

    @Test
    @DisplayName("Deve atualizar status corretamente")
    void testAtualizarStatus() {
        manutencao.setStatus(StatusManutencao.EM_ANDAMENTO);
        assertEquals(StatusManutencao.EM_ANDAMENTO, manutencao.getStatus());
        
        manutencao.setStatus(StatusManutencao.CONCLUIDA);
        assertEquals(StatusManutencao.CONCLUIDA, manutencao.getStatus());
    }

    @Test
    @DisplayName("Deve atualizar ID do funcionário corretamente")
    void testAtualizarIdFuncionario() {
        Integer novoIdFuncionario = 2;
        manutencao.setIdFuncionario(novoIdFuncionario);
        assertEquals(novoIdFuncionario, manutencao.getIdFuncionario());
    }

    @Test
    @DisplayName("Deve aceitar descrição nula")
    void testDescricaoNula() {
        manutencao.setDescricao(null);
        assertNull(manutencao.getDescricao());
    }

    @Test
    @DisplayName("Deve aceitar data de fim nula")
    void testDataFimNula() {
        manutencao.setDataFim(null);
        assertNull(manutencao.getDataFim());
    }
} 