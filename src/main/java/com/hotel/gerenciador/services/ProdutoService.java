package com.hotel.gerenciador.services;

import com.hotel.gerenciador.daos.ProdutoDAO;
import com.hotel.gerenciador.models.Produto;
import com.hotel.gerenciador.utils.CategoriaProduto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO(); 
    }

    public boolean addProduto(Produto produto) {
        try {
            return produtoDAO.insert(produto);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upProduto(Produto produto) {
        try {
            return produtoDAO.update(produto);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delProduto(int produtoId) {
        try {
            return produtoDAO.delete(produtoId); 
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false; 
        }
    }

    public Produto findProdutoPorId(int produtoId) {
        try {
            return produtoDAO.findById(produtoId); 
        } catch (SQLException e) {
            e.printStackTrace(); 
            return null; 
        }
    }

    public boolean alterarCategoriaProduto(int produtoId, CategoriaProduto novaCategoria) {
        try {
            Produto produto = produtoDAO.findById(produtoId); 
            if (produto != null) { 
                produto.setCategoria(novaCategoria); 
                return produtoDAO.update(produto); 
            }
            return false; 
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false; 
        }
    }

    public List<Produto> getAllProdutos() {
        try {
            return produtoDAO.findAll(); 
        } catch (SQLException e) {
            e.printStackTrace(); 
            return new ArrayList<>();
        }
    }
}