package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.ServicoDAO;
import com.hotel.gerenciador.model.Servico;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicoService {

    private final ServicoDAO servicoDAO;

    public ServicoService() {
        this.servicoDAO = new ServicoDAO();
    }

    public boolean addServico(Servico servico) {
        if (servico.getPreco() == null || servico.getPreco().compareTo(new BigDecimal("20.00")) <= 0) {
            throw new IllegalArgumentException("O preço do serviço deve ser superior a 20 reais.");
        }

        try {
            return servicoDAO.insert(servico);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upServico(Servico servico) {
        if (servico.getPreco() == null || servico.getPreco().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O preço do serviço não pode ser negativo.");
        }

        try {
            return servicoDAO.update(servico);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delServico(int servicoId) {
        try {
            return servicoDAO.delete(servicoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Servico findServicoPorId(int servicoId) {
        try {
            return servicoDAO.findById(servicoId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Servico> getAllServicos() {
        try {
            return servicoDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}