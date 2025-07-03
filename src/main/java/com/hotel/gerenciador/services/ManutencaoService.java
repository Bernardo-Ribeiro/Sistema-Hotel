package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.ManutencaoDAO;
import com.hotel.gerenciador.models.Manutencao;
import com.hotel.gerenciador.utils.StatusManutencao;

import java.sql.SQLException;
import java.util.List;

public class ManutencaoService {

    private ManutencaoDAO manutencaoDAO;

    public ManutencaoService() {
        this.manutencaoDAO = new ManutencaoDAO();
    }

    public boolean addManutencao(Manutencao manutencao) {
        try {
            return manutencaoDAO.insert(manutencao);
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar manutenção: " + e.getMessage());
            return false;
        }
    }

    public boolean upStatus(int idManutencao, StatusManutencao status) {
        try {
            Manutencao manutencao = manutencaoDAO.findById(idManutencao);
            if (manutencao != null) {
                manutencao.setStatus(status);
                return manutencaoDAO.update(manutencao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o status da manutenção: " + e.getMessage());
        }
        return false;
    }

    public List<Manutencao> listManutencaoPorStatus(StatusManutencao status) {
        try {
            return manutencaoDAO.findByStatus(status);
        } catch (SQLException e) {
            System.out.println("Erro ao listar manutenções: " + e.getMessage());
            return null;
        }
    }

    public Manutencao findManutencaoPorId(int idManutencao) {
        try {
            return manutencaoDAO.findById(idManutencao);
        } catch (SQLException e) {
            System.out.println("Erro ao buscar manutenção: " + e.getMessage());
            return null;
        }
    }
    public boolean updateManutencao(Manutencao manutencao) {
        try {
            return manutencaoDAO.update(manutencao);
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar manutenção: " + e.getMessage());
            return false;
        }
    }
    public List<Manutencao> getAllManutencoes() {
        try {
            return manutencaoDAO.findAll();
        } catch (SQLException e) {
            System.out.println("Erro ao listar todas as manutenções: " + e.getMessage());
            return null;
        }
    }

    public boolean deleteManutencao(int idManutencao) {
        try {
            return manutencaoDAO.delete(idManutencao);
        } catch (SQLException e) {
            System.out.println("Erro ao excluir manutenção: " + e.getMessage());
            return false;
        }
    }

}
