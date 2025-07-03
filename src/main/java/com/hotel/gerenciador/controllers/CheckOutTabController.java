package com.hotel.gerenciador.controllers;

import com.hotel.gerenciador.models.*;
import com.hotel.gerenciador.services.*;
import com.hotel.gerenciador.utils.*;
import com.hotel.gerenciador.viewmodels.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.*;

public class CheckOutTabController extends BaseController {
    @FXML private TableView<ReservaViewModel> tblHospedesSaidas;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaHospede;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaQuarto;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaDtCheckOutPrevista;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaSaldo;

    @FXML private VBox paneDetalhesCheckOut;
    @FXML private Label lblCONomeHospede, lblCONumQuarto, lblCODatasEstadia;
    
    @FXML private ComboBox<ItemCobrancaViewModel> cmbCOItemParaAdicionar;
    @FXML private TextField txtCOConsumoQuantidade;
    @FXML private Button btnCOAdicionarConsumo;

    @FXML private TableView<String> tblCOContaConsumo; 
    @FXML private TableColumn<String, String> colCOItemConsumo;
    @FXML private Label lblCOTotalReserva, lblCOTotalConsumos, lblCOTotalConta;
    @FXML private Label lblCOValorJaPago, lblCOSaldoDevedor;
    @FXML private CheckBox chkCOConsumosConfirmados;
    @FXML private ComboBox<MetodoPagamento> cmbCOMetodoPagamento;
    @FXML private TextField txtCOValorPago;
    @FXML private Button btnCORegistrarPagamento;
    @FXML private Button btnCOConfirmarCheckOut;

    private final ReservaService reservaService;
    private final QuartoService quartoService;
    private final PagamentoService pagamentoService;
    private final ConsumoService produtoConsumoService;
    private final ConsumoServicosService servicoConsumoService;
    private final ProdutoService produtoService;
    private final ServicoService servicoService;
    private final CalculadoraFinanceira calculadoraFinanceira;

    private final ObservableList<ReservaViewModel> listaHospedesSaidas;
    private Reserva reservaSelecionada;

    public CheckOutTabController() {
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        this.pagamentoService = new PagamentoService();
        this.produtoConsumoService = new ConsumoService();
        this.servicoConsumoService = new ConsumoServicosService();
        this.produtoService = new ProdutoService();
        this.servicoService = new ServicoService();
        
        this.calculadoraFinanceira = new CalculadoraFinanceira(
            pagamentoService, produtoConsumoService, servicoConsumoService);
            
        this.listaHospedesSaidas = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        configurarTabela();
        configurarComboBoxes();
        configurarEventos();
    }

    private void configurarTabela() {
        colSaidaHospede.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        colSaidaQuarto.setCellValueFactory(cellData -> cellData.getValue().quartoProperty());
        colSaidaDtCheckOutPrevista.setCellValueFactory(cellData -> cellData.getValue().dataSaidaProperty());
        colSaidaSaldo.setCellValueFactory(cellData -> {
            ReservaViewModel rvm = cellData.getValue();
            if (rvm != null) {
                Reserva r = reservaService.findReservaPorId(rvm.idProperty().get());
                if (r != null) {
                    BigDecimal saldoDevedor = calculadoraFinanceira.calcularSaldoDevedorReserva(r);
                    return new SimpleStringProperty(Formatter.formatCurrency(saldoDevedor));
                }
            }
            return new SimpleStringProperty(Formatter.formatCurrency(BigDecimal.ZERO));
        });

        tblHospedesSaidas.setItems(listaHospedesSaidas);
        tblHospedesSaidas.setPlaceholder(new Label("Nenhum hóspede na casa ou saída para a data/filtros."));
        
        tblHospedesSaidas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelectionVM) -> {
                Reserva novaReservaSelecionada = null;
                if (newSelectionVM != null) {
                    novaReservaSelecionada = reservaService.findReservaPorId(newSelectionVM.idProperty().get());
                }
                this.reservaSelecionada = novaReservaSelecionada;
                mostrarDetalhesCheckOut(novaReservaSelecionada);
            });
        
        colCOItemConsumo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
    }

    private void configurarComboBoxes() {
        // Configurar ComboBox de Itens para Cobrança
        cmbCOItemParaAdicionar.setConverter(new StringConverter<ItemCobrancaViewModel>() {
            @Override
            public String toString(ItemCobrancaViewModel object) {
                return object == null ? "Selecione um item..." : object.toString();
            }
            @Override
            public ItemCobrancaViewModel fromString(String string) {
                return null;
            }
        });

        // Configurar ComboBox de Método de Pagamento
        StringConverter<MetodoPagamento> metodoPagamentoConverter = new StringConverter<MetodoPagamento>() {
            @Override
            public String toString(MetodoPagamento metodo) {
                return metodo == null ? "Selecione" : metodo.toString().replace("_", " ");
            }
            @Override
            public MetodoPagamento fromString(String string) {
                if (string == null || string.equals("Selecione")) return null;
                try {
                    return MetodoPagamento.valueOf(string.replace(" ", "_"));
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        };

        cmbCOMetodoPagamento.setItems(FXCollections.observableArrayList(MetodoPagamento.values()));
        cmbCOMetodoPagamento.setConverter(metodoPagamentoConverter);
    }

    private void configurarEventos() {
        btnCOAdicionarConsumo.setOnAction(event -> handleAdicionarConsumo());
        btnCORegistrarPagamento.setOnAction(event -> handleRegistrarPagamentoFinal());
        btnCOConfirmarCheckOut.setOnAction(event -> handleConfirmarCheckOut());
    }

    @FXML
    private void handleAdicionarConsumo() {
        try {
            Validator.validateNotNull(reservaSelecionada, "Reserva");
            Validator.validateNotNull(cmbCOItemParaAdicionar.getValue(), "Item para consumo");
            
            String quantidadeStr = txtCOConsumoQuantidade.getText();
            if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Quantidade é obrigatória.");
            }

            int quantidade = Integer.parseInt(quantidadeStr);
            Validator.validatePositiveValue(quantidade);

            ItemCobrancaViewModel item = cmbCOItemParaAdicionar.getValue();
            
            if (item.isProduto()) {
                Produto produto = (Produto) item.getItem();
                Consumo consumo = new Consumo();
                consumo.setIdReserva(reservaSelecionada.getId());
                consumo.setIdProduto(produto.getId());
                consumo.setQuantidade(quantidade);
                consumo.setValor(produto.getPreco().doubleValue());
                consumo.setDataConsumo(LocalDate.now());
                
                if (!produtoConsumoService.addConsumo(consumo)) {
                    throw new Exception("Não foi possível registrar o consumo do produto.");
                }
            } else {
                Servico servico = (Servico) item.getItem();
                ConsumoServicos consumoServico = new ConsumoServicos();
                consumoServico.setReservaId(reservaSelecionada.getId());
                consumoServico.setServicoId(servico.getId());
                consumoServico.setQuantidade(quantidade);
                consumoServico.setDataConsumo(LocalDate.now());
                
                if (!servicoConsumoService.addConsumo(consumoServico)) {
                    throw new Exception("Não foi possível registrar o consumo do serviço.");
                }
            }

            mostrarAlerta("Sucesso", "Consumo registrado com sucesso!");
            mostrarDetalhesCheckOut(reservaSelecionada);
            txtCOConsumoQuantidade.clear();
            cmbCOItemParaAdicionar.setValue(null);
            
        } catch (NumberFormatException e) {
            mostrarAlerta("Erro de Validação", "Quantidade deve ser um número inteiro válido.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro ao registrar o consumo: " + e.getMessage());
        }
    }

    @FXML
    private void handleRegistrarPagamentoFinal() {
        try {
            Validator.validateNotNull(reservaSelecionada, "Reserva");
            Validator.validateNotNull(cmbCOMetodoPagamento.getValue(), "Método de pagamento");

            String valorStr = txtCOValorPago.getText();
            BigDecimal valor = parseValorPagamento(valorStr);
            Validator.validatePositiveValue(valor);

            Pagamento pagamento = new Pagamento();
            pagamento.setReservaId(reservaSelecionada.getId());
            pagamento.setValor(valor);
            pagamento.setMetodo(cmbCOMetodoPagamento.getValue());
            pagamento.setStatus(StatusPagamento.PAGO);
            pagamento.setDataPagamento(LocalDate.now());

            if (pagamentoService.addPagamento(pagamento)) {
                mostrarAlerta("Sucesso", "Pagamento registrado com sucesso!");
                mostrarDetalhesCheckOut(reservaSelecionada);
                txtCOValorPago.clear();
                cmbCOMetodoPagamento.setValue(null);
            } else {
                throw new Exception("Não foi possível registrar o pagamento.");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro ao registrar o pagamento: " + e.getMessage());
        }
    }

    @FXML
    private void handleConfirmarCheckOut() {
        try {
            Validator.validateNotNull(reservaSelecionada, "Reserva");
            Validator.validateTrue(chkCOConsumosConfirmados.isSelected(), 
                "Confirme a verificação dos consumos");

            // Verificar se há saldo pendente
            BigDecimal saldoDevedor = calculadoraFinanceira.calcularSaldoDevedorReserva(reservaSelecionada);
            if (saldoDevedor.compareTo(BigDecimal.ZERO) > 0) {
                throw new IllegalArgumentException(
                    String.format("Existe saldo pendente de %s. Realize o pagamento antes do check-out.",
                        Formatter.formatCurrency(saldoDevedor)));
            }

            // Atualizar status da reserva
            reservaSelecionada.setStatus(StatusReserva.CONCLUIDA);

            // Atualizar status do quarto
            Quarto quarto = quartoService.findQuartoPorId(reservaSelecionada.getQuarto().getId());
            if (quarto != null) {
                quarto.setStatus(StatusQuarto.MANUTENCAO);
            }

            if (reservaService.upReserva(reservaSelecionada) && 
                (quarto == null || quartoService.upQuarto(quarto))) {
                mostrarAlerta("Sucesso", "Check-out realizado com sucesso!");
                limparDetalhesCheckOut();
                atualizarListaHospedesSaidas(LocalDate.now(), "");
            } else {
                throw new Exception("Não foi possível realizar o check-out.");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro ao realizar o check-out: " + e.getMessage());
        }
    }

    private void mostrarDetalhesCheckOut(Reserva reserva) {
        if (reserva == null) {
            limparDetalhesCheckOut();
            return;
        }

        // Tornar o painel visível
        paneDetalhesCheckOut.setVisible(true);
        paneDetalhesCheckOut.setManaged(true);

        // Preencher dados do hóspede
        lblCONomeHospede.setText(reserva.getHospede().getNome());
        lblCONumQuarto.setText(String.valueOf(reserva.getQuarto().getNumeroQuarto()));
        lblCODatasEstadia.setText(String.format("%s a %s",
            Formatter.formatDate(reserva.getDataCheckIn()),
            Formatter.formatDate(reserva.getDataCheckOut())));

        // Atualizar valores
        BigDecimal valorReserva = reserva.getValorTotal();
        BigDecimal totalConsumos = calculadoraFinanceira.calcularTotalProdutoConsumos(reserva.getId())
            .add(calculadoraFinanceira.calcularTotalServicoConsumos(reserva.getId()));
        BigDecimal totalPago = calculadoraFinanceira.calcularTotalPagoParaReserva(reserva.getId());
        BigDecimal saldoDevedor = calculadoraFinanceira.calcularSaldoDevedorReserva(reserva);

        lblCOTotalReserva.setText(Formatter.formatCurrency(valorReserva));
        lblCOTotalConsumos.setText(Formatter.formatCurrency(totalConsumos));
        lblCOTotalConta.setText(Formatter.formatCurrency(valorReserva.add(totalConsumos)));
        lblCOValorJaPago.setText(Formatter.formatCurrency(totalPago));
        lblCOSaldoDevedor.setText(Formatter.formatCurrency(saldoDevedor));

        // Atualizar lista de consumos
        atualizarListaConsumos(reserva);

        // Carregar itens para cobrança
        popularItensCobrancaComboBox();

        // Habilitar/desabilitar controles
        boolean podeFazerCheckOut = reserva.getStatus() == StatusReserva.HOSPEDADO;
        btnCOConfirmarCheckOut.setDisable(!podeFazerCheckOut);
        chkCOConsumosConfirmados.setDisable(!podeFazerCheckOut);
    }

    private void limparDetalhesCheckOut() {
        lblCONomeHospede.setText("");
        lblCONumQuarto.setText("");
        lblCODatasEstadia.setText("");
        lblCOTotalReserva.setText("");
        lblCOTotalConsumos.setText("");
        lblCOTotalConta.setText("");
        lblCOValorJaPago.setText("");
        lblCOSaldoDevedor.setText("");
        
        tblCOContaConsumo.getItems().clear();
        cmbCOItemParaAdicionar.getItems().clear();
        txtCOConsumoQuantidade.clear();
        chkCOConsumosConfirmados.setSelected(false);
        txtCOValorPago.clear();
        cmbCOMetodoPagamento.setValue(null);
        
        btnCOConfirmarCheckOut.setDisable(true);
        chkCOConsumosConfirmados.setDisable(true);

        // Esconder o painel
        paneDetalhesCheckOut.setVisible(false);
        paneDetalhesCheckOut.setManaged(false);
    }

    public void atualizarListaHospedesSaidas(LocalDate data, String termoBusca) {
        carregarHospedesSaidas(data, termoBusca);
    }

    private void carregarHospedesSaidas(LocalDate data, String termoBusca) {
        List<Reserva> reservas = reservaService.buscarReservasComFiltros(StatusReserva.HOSPEDADO, data, data, termoBusca);
        listaHospedesSaidas.clear();
        listaHospedesSaidas.addAll(reservas.stream()
            .map(this::toViewModel)
            .toList());
    }

    private void atualizarListaConsumos(Reserva reserva) {
        List<String> consumos = new ArrayList<>();
        
        // Adicionar consumos de produtos
        List<Consumo> consumosProdutos = produtoConsumoService.findConsumosPorReserva(reserva.getId());
        for (Consumo consumo : consumosProdutos) {
            Produto produto = produtoService.findProdutoPorId(consumo.getIdProduto());
            if (produto != null) {
                consumos.add(String.format("%s x%d - %s",
                    produto.getNome(),
                    consumo.getQuantidade(),
                    Formatter.formatCurrency(consumo.getValor())));
            }
        }
        
        // Adicionar consumos de serviços
        List<ConsumoServicos> consumoServicos = servicoConsumoService.findConsumosPorReserva(reserva.getId());
        for (ConsumoServicos consumoServico : consumoServicos) {
            Servico servico = servicoService.findServicoPorId(consumoServico.getServicoId());
            if (servico != null) {
                consumos.add(String.format("%s x%d - %s",
                    servico.getNome(),
                    consumoServico.getQuantidade(),
                    Formatter.formatCurrency(servico.getPreco().doubleValue())));
            }
        }
        
        tblCOContaConsumo.getItems().setAll(consumos);
    }

    private void popularItensCobrancaComboBox() {
        List<ItemCobrancaViewModel> itens = new ArrayList<>();
        
        // Adicionar produtos
        List<Produto> produtos = produtoService.getAllProdutos();
        for (Produto produto : produtos) {
            itens.add(new ItemCobrancaViewModel(produto));
        }
        
        // Adicionar serviços
        List<Servico> servicos = servicoService.getAllServicos();
        for (Servico servico : servicos) {
            itens.add(new ItemCobrancaViewModel(servico));
        }
        
        cmbCOItemParaAdicionar.setItems(FXCollections.observableArrayList(itens));
    }

    private ReservaViewModel toViewModel(Reserva reserva) {
        if (reserva == null) return null;
        String nomeHospede = (reserva.getHospede() != null && reserva.getHospede().getNome() != null) ? reserva.getHospede().getNome() : "N/D";
        String numeroQuarto = (reserva.getQuarto() != null) ? String.valueOf(reserva.getQuarto().getNumeroQuarto()) : "N/D";
        String dataCheckInFormatada = (reserva.getDataCheckIn() != null) ? Formatter.formatDate(reserva.getDataCheckIn()) : "";
        String dataCheckOutFormatada = (reserva.getDataCheckOut() != null) ? Formatter.formatDate(reserva.getDataCheckOut()) : "";
        
        StringProperty valorDisplayProperty = new SimpleStringProperty(
            (reserva.getValorTotal() != null) ? Formatter.formatCurrency(reserva.getValorTotal()) : Formatter.formatCurrency(BigDecimal.ZERO)
        );
        String statusReserva = (reserva.getStatus() != null) ? reserva.getStatus().toString() : "";

        return new ReservaViewModel(
            reserva.getId(), nomeHospede, numeroQuarto,
            dataCheckInFormatada, dataCheckOutFormatada, statusReserva,
            valorDisplayProperty
        );
    }

    private BigDecimal parseValorPagamento(String valorStr) {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Valor do pagamento é obrigatório.");
        }
        
        try {
            return new BigDecimal(valorStr.replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de valor inválido. Use: 1234,56");
        }
    }
} 