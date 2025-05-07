package com.hotel.gerenciador;

import com.hotel.gerenciador.dao.*;
import com.hotel.gerenciador.model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            ConsumoDAO dao = new ConsumoDAO();
            List<Consumo> consumo = dao.findAll();
            
            System.out.println("Lista de Consumos: ");
            for (Consumo c : consumo) {
                System.out.println(c); // toString() é chamado automaticamente aqui
            }            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
