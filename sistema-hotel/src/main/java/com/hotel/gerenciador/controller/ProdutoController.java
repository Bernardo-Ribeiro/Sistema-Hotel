package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Produto;
import com.hotel.gerenciador.service.ProdutoService;
import com.hotel.gerenciador.util.CategoriaProduto;

public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController() {
        this.produtoService = new ProdutoService();
    }

    public boolean cadastrarProduto(Produto produto) {
        return produtoService.addProduto(produto);
    }

    public boolean atualizarProduto(Produto produto) {
        return produtoService.upProduto(produto);
    }

    public boolean deletarProduto(int produtoId) {
        return produtoService.delProduto(produtoId);
    }

    public Produto buscarProdutoPorId(int produtoId) {
        return produtoService.findProdutoPorId(produtoId);
    }

    public boolean alterarCategoria(int produtoId, CategoriaProduto novaCategoria) {
        return produtoService.alterarCategoriaProduto(produtoId, novaCategoria);
    }
}
