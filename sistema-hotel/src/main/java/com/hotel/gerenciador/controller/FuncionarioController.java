package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Funcionario;
import com.hotel.gerenciador.service.FuncionarioService;

import java.util.List;

public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    public FuncionarioController() {
        this.funcionarioService = new FuncionarioService();
    }

    public boolean cadastrarFuncionario(Funcionario funcionario) {
        return funcionarioService.addFuncionario(funcionario);
    }

    public boolean atualizarFuncionario(Funcionario funcionario) {
        return funcionarioService.upFuncionario(funcionario);
    }

    public boolean removerFuncionario(int id) {
        return funcionarioService.delFuncionario(id);
    }

    public Funcionario buscarFuncionarioPorId(int id) {
        return funcionarioService.findFuncionarioPorId(id);
    }

    public Funcionario buscarFuncionarioPorCpf(String cpf) {
        return funcionarioService.findFuncionarioPorCpf(cpf);
    }

    public List<Funcionario> buscarFuncionariosPorNome(String nome) {
        return funcionarioService.findFuncionariosPorNome(nome);
    }

    public List<Funcionario> listarTodosFuncionarios() {
        return funcionarioService.getAllFuncionarios();
    }
}
