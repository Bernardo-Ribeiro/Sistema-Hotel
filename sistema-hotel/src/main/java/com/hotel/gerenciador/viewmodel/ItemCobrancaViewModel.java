package com.hotel.gerenciador.viewmodel;

import com.hotel.gerenciador.model.Produto;
import com.hotel.gerenciador.model.Servico;
import com.hotel.gerenciador.util.Formatter;
import java.math.BigDecimal;

public class ItemCobrancaViewModel {
    private final Object item;
    private final String displayName;
    private final BigDecimal price;
    private final boolean isProduto;
    private final int idOriginal;

    public ItemCobrancaViewModel(Produto produto) {
        this.item = produto;
        this.displayName = "PRODUTO: " + produto.getNome();
        this.price = produto.getPreco();
        this.isProduto = true;
        this.idOriginal = produto.getId();
    }

    public ItemCobrancaViewModel(Servico servico) {
        this.item = servico;
        this.displayName = "SERVIÇO: " + servico.getNome();
        this.price = servico.getPreco();
        this.isProduto = false;
        this.idOriginal = servico.getId();
    }

    public Object getItem() { return item; }
    public BigDecimal getPrice() { return price; }
    public boolean isProduto() { return isProduto; }
    public int getIdOriginal() { return idOriginal; }

    @Override
    public String toString() { 
        return displayName + (price != null && price.compareTo(BigDecimal.ZERO) >= 0 ?
                              " - " + Formatter.formatCurrency(price) : ""); 
    }
} 