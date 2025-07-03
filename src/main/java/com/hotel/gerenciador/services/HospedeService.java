package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.HospedeDAO;
import com.hotel.gerenciador.models.Hospede;
import com.hotel.gerenciador.utils.Validator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HospedeService {

    private final HospedeDAO hospedeDAO;

    public HospedeService() {
        this.hospedeDAO = new HospedeDAO();
    }

    public boolean addHospede(Hospede hospede) {
        try {
            return hospedeDAO.insert(hospede);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateHospede(Hospede hospede) {
        try {
            Hospede existenteComCpf = hospedeDAO.findByCpf(hospede.getCpf());
            if (existenteComCpf != null && existenteComCpf.getId() != hospede.getId()) {
                throw new IllegalArgumentException("CPF já cadastrado para outro hóspede.");
            }
            return hospedeDAO.update(hospede);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteHospede(int hospedeId) {
        try {
            return hospedeDAO.delete(hospedeId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Hospede findHospedeById(int hospedeId) {
        try {
            return hospedeDAO.findById(hospedeId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Hospede> findHospedesByName(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return getAllHospedes(); 
        }
        try {
            return hospedeDAO.findByName(nome);
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public Hospede findHospedeByCpf(String cpf) {
        Validator.validateCpf(cpf);
        try {
            return hospedeDAO.findByCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Hospede> getAllHospedes() {
        try {
            return hospedeDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os hóspedes: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    public List<Hospede> findHospedeByNameOrCPF(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return getAllHospedes();
        }
        try {
            return hospedeDAO.findByNameOrCPF(termo);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public boolean isCpfEmUso(String cpf, int idExcluir) {
        try {
            return hospedeDAO.isCpfInUse(cpf, idExcluir); //
        } catch (SQLException e) {
            e.printStackTrace();
            return false; 
        }
    }
}