package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.FuncionarioDAO;
import com.hotel.gerenciador.models.Funcionario;

import java.sql.SQLException;
import java.util.List;

public class FuncionarioService {

    private final FuncionarioDAO funcionarioDAO;

    public FuncionarioService() {
        this.funcionarioDAO = new FuncionarioDAO();
    }

    public boolean addFuncionario(Funcionario funcionario) {
        try {
            return funcionarioDAO.insert(funcionario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upFuncionario(Funcionario funcionario) {
        try {
            return funcionarioDAO.update(funcionario);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delFuncionario(int funcionarioId) {
        try {
            return funcionarioDAO.delete(funcionarioId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Funcionario findFuncionarioPorId(int funcionarioId) {
        try {
            return funcionarioDAO.findById(funcionarioId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Funcionario findFuncionarioPorCpf(String cpf) {
        try {
            return funcionarioDAO.findByCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Funcionario> findFuncionariosPorNome(String nome) {
        try {
            return funcionarioDAO.findByName(nome);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Funcionario> getAllFuncionarios() {
        try {
            return funcionarioDAO.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
