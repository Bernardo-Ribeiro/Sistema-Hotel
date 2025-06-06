package com.hotel.gerenciador.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.hotel.gerenciador.util.CategoriaProduto;

public class ProdutoTest {
    private Produto produto;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @BeforeEach
    void setUp() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
        
        produto = new Produto(
            1,
            "Água Mineral",
            "Garrafa de água mineral 500ml",
            new BigDecimal("5.00"),
            100,
            CategoriaProduto.BEBIDA,
            dataCriacao,
            dataAtualizacao
        );
    }

    @Test
    @DisplayName("Deve criar um produto com dados válidos")
    void testCriarProdutoComDadosValidos() {
        assertNotNull(produto);
        assertEquals(1, produto.getId());
        assertEquals("Água Mineral", produto.getNome());
        assertEquals("Garrafa de água mineral 500ml", produto.getDescricao());
        assertEquals(new BigDecimal("5.00"), produto.getPreco());
        assertEquals(100, produto.getEstoque());
        assertEquals(CategoriaProduto.BEBIDA, produto.getCategoria());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir nome nulo")
    void testSetNomeNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setNome(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir nome vazio")
    void testSetNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setNome("   ");
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir descrição muito longa")
    void testSetDescricaoMuitoLonga() {
        String descricaoLonga = "a".repeat(256);
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setDescricao(descricaoLonga);
        });
    }

    @Test
    @DisplayName("Deve aceitar descrição nula")
    void testSetDescricaoNula() {
        produto.setDescricao(null);
        assertEquals("", produto.getDescricao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir preço nulo")
    void testSetPrecoNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setPreco(null);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir preço zero")
    void testSetPrecoZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setPreco(BigDecimal.ZERO);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir preço negativo")
    void testSetPrecoNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setPreco(new BigDecimal("-10.00"));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir estoque negativo")
    void testSetEstoqueNegativo() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setEstoque(-1);
        });
    }

    @Test
    @DisplayName("Deve aceitar estoque zero")
    void testSetEstoqueZero() {
        produto.setEstoque(0);
        assertEquals(0, produto.getEstoque());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir categoria nula")
    void testSetCategoriaNula() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setCategoria(null);
        });
    }

    @Test
    @DisplayName("Deve atualizar dados do produto corretamente")
    void testAtualizarDadosProduto() {
        String novoNome = "Água Mineral Premium";
        String novaDescricao = "Garrafa de água mineral 1L";
        BigDecimal novoPreco = new BigDecimal("7.50");
        int novoEstoque = 50;
        CategoriaProduto novaCategoria = CategoriaProduto.BEBIDA;
        
        produto.setNome(novoNome);
        produto.setDescricao(novaDescricao);
        produto.setPreco(novoPreco);
        produto.setEstoque(novoEstoque);
        produto.setCategoria(novaCategoria);
        
        assertEquals(novoNome, produto.getNome());
        assertEquals(novaDescricao, produto.getDescricao());
        assertEquals(novoPreco, produto.getPreco());
        assertEquals(novoEstoque, produto.getEstoque());
        assertEquals(novaCategoria, produto.getCategoria());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de criação futura")
    void testSetDataCriacaoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setDataCriacao(LocalDateTime.now().plusDays(1));
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar definir data de atualização futura")
    void testSetDataAtualizacaoFutura() {
        assertThrows(IllegalArgumentException.class, () -> {
            produto.setDataAtualizacao(LocalDateTime.now().plusDays(1));
        });
    }
} 