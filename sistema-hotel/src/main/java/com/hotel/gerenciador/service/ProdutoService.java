package com.hotel.gerenciador.service;

import com.hotel.gerenciador.dao.ProdutoDAO;
import com.hotel.gerenciador.model.Produto;
import com.hotel.gerenciador.util.CategoriaProduto;

import java.sql.SQLException;

public class ProdutoService {

    private final ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public boolean addProduto(Produto produto) {
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }

        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }

        try {
            return produtoDAO.insert(produto);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean upProduto(Produto produto) {
        if (produto.getPreco() <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser maior que zero.");
        }

        if (produto.getNome() == null || produto.getNome().isEmpty()) {
            throw new IllegalArgumentException("O nome do produto não pode ser vazio.");
        }

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
}
