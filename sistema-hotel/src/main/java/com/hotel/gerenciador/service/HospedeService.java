package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.HospedeDAO;
import com.hotel.gerenciador.model.Hospede;

import java.sql.SQLException;
import java.util.List;

public class HospedeService {

    private final HospedeDAO hospedeDAO;

    public HospedeService() {
        this.hospedeDAO = new HospedeDAO();
    }

    public boolean addHospede(Hospede hospede) {
        if (hospede.getCpf() == null || hospede.getCpf().isEmpty()) {
            throw new IllegalArgumentException("O CPF do hóspede é obrigatório.");
        }

        try {
            return hospedeDAO.insert(hospede);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateHospede(Hospede hospede) {
        if (hospede.getCpf() == null || hospede.getCpf().isEmpty()) {
            throw new IllegalArgumentException("O CPF do hóspede é obrigatório.");
        }

        try {
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
        try {
            return hospedeDAO.findByName(nome);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Hospede findHospedeByCpf(String cpf) {
        try {
            return hospedeDAO.findByCpf(cpf);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
