package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Hospede;
import com.hotel.gerenciador.service.HospedeService;

import java.util.List;

public class HospedeController {

    private final HospedeService hospedeService;

    public HospedeController() {
        this.hospedeService = new HospedeService();
    }

    public boolean cadastrarHospede(Hospede hospede) {
        return hospedeService.addHospede(hospede);
    }

    public boolean atualizarHospede(Hospede hospede) {
        return hospedeService.updateHospede(hospede);
    }

    public boolean removerHospede(int id) {
        return hospedeService.deleteHospede(id);
    }

    public Hospede buscarHospedePorId(int id) {
        return hospedeService.findHospedeById(id);
    }

    public Hospede buscarHospedePorCpf(String cpf) {
        return hospedeService.findHospedeByCpf(cpf);
    }

    public List<Hospede> buscarHospedesPorNome(String nome) {
        return hospedeService.findHospedesByName(nome);
    }
}