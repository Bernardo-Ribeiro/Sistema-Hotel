package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.model.Pagamento;
import com.hotel.gerenciador.model.Consumo;
import com.hotel.gerenciador.model.ConsumoServicos;
import com.hotel.gerenciador.model.Produto;
import com.hotel.gerenciador.model.Servico;
import com.hotel.gerenciador.viewmodel.ReservaViewModel;
import com.hotel.gerenciador.service.HospedeService;
import com.hotel.gerenciador.service.QuartoService;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.service.PagamentoService;
import com.hotel.gerenciador.service.ConsumoService;
import com.hotel.gerenciador.service.ConsumoServicosService;
import com.hotel.gerenciador.service.ProdutoService;
import com.hotel.gerenciador.service.ServicoService;

import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.StatusPagamento;
// import com.hotel.gerenciador.util.TipoQuarto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CheckInController {

    public static class ItemCobrancaViewModel {
        private Object item;
        private String displayName;
        private java.math.BigDecimal price;
        private boolean isProduto;
        private int idOriginal;

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
        public java.math.BigDecimal getPrice() { return price; }
        public boolean isProduto() { return isProduto; }
        public int getIdOriginal() { return idOriginal; }

        @Override
        public String toString() { 
            return displayName + (price != null && price.compareTo(java.math.BigDecimal.ZERO) >= 0 ?
                                  " - " + Formatter.formatCurrency(price) : ""); 
        }
    }

    // == Campos FXML ==
    @FXML private DatePicker dateFiltroReferencia;
    @FXML private TextField txtBuscaGeral;
    @FXML private Button btnBuscaGeral;

    @FXML private TabPane tabPanePrincipal;
    @FXML private Tab tabCheckIn;
    @FXML private Tab tabCheckOut;

    // -- Check-In Tab --
    @FXML private Label lblChegadasData;
    @FXML private TableView<ReservaViewModel> tblChegadas;
    @FXML private TableColumn<ReservaViewModel, Integer> colChegadaIdReserva;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaHospede;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaQuarto;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaDtCheckIn;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaDtCheckOut;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaStatusReserva;
    
    @FXML private VBox paneDetalhesCheckIn;
    @FXML private Label lblCINomeHospede, lblCICPF, lblCITelefone, lblCIEmail;
    @FXML private Label lblCINumQuarto, lblCITipoQuarto, lblCIDatasEstadia, lblCIValorTotalReserva, lblCIStatusPagamento;
    @FXML private ComboBox<Quarto> cmbCIQuartoDisponivel;
    @FXML private CheckBox chkCIDocumentosConfirmados;
    @FXML private Button btnConfirmarCheckIn;
    @FXML private ComboBox<MetodoPagamento> cmbCIPagamentoMetodo;
    @FXML private TextField txtCIPagamentoValor;
    @FXML private Button btnCIRegistrarPagamento;
    @FXML private Label lblCISaldoPendente;


    // -- Check-Out Tab --
    @FXML private Label lblHospedesSaidasData;
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
    @FXML private Label lblCOTotalReserva, lblCOTotalConsumos, lblCOTotalConta, lblCOValorJaPago, lblCOSaldoDevedor;
    @FXML private CheckBox chkCOConsumosConfirmados;
    @FXML private ComboBox<MetodoPagamento> cmbCOMetodoPagamento;
    @FXML private TextField txtCOValorPago;
    @FXML private Button btnCORegistrarPagamento;
    @FXML private Button btnCOConfirmarCheckOut;

    private ReservaService reservaService;
    private QuartoService quartoService;
    private PagamentoService pagamentoService;
    private ConsumoService produtoConsumoService;
    private ConsumoServicosService servicoConsumoService;
    private ProdutoService produtoService; 
    private ServicoService servicoService; 

    private ObservableList<ReservaViewModel> listaChegadas = FXCollections.observableArrayList();
    private ObservableList<ReservaViewModel> listaHospedesSaidas = FXCollections.observableArrayList();
    
    private Reserva reservaSelecionadaParaCheckIn;
    private Reserva reservaSelecionadaParaCheckOut;

    public CheckInController() {
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        new HospedeService(); 
        this.pagamentoService = new PagamentoService();
        this.produtoConsumoService = new ConsumoService();
        this.servicoConsumoService = new ConsumoServicosService();
        this.produtoService = new ProdutoService(); 
        this.servicoService = new ServicoService(); 
    }

    @FXML
    private void initialize() {
        configurarTabelas();
        configurarFiltrosEEventos();
        configurarPainelDetalhes();
        configurarAdicionarConsumo();
        
        dateFiltroReferencia.setValue(LocalDate.now());
        atualizarTodasAsListas();
    }

    private void configurarAdicionarConsumo() {
        if (btnCOAdicionarConsumo != null) {
            btnCOAdicionarConsumo.setOnAction(event -> handleAdicionarConsumo());
        }
        if (cmbCOItemParaAdicionar != null) {
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
        }
    }
    
    private void configurarTabelas() {
        colChegadaIdReserva.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colChegadaHospede.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        colChegadaQuarto.setCellValueFactory(cellData -> cellData.getValue().quartoProperty());
        colChegadaDtCheckIn.setCellValueFactory(cellData -> cellData.getValue().dataEntradaProperty());
        colChegadaDtCheckOut.setCellValueFactory(cellData -> cellData.getValue().dataSaidaProperty());
        colChegadaStatusReserva.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        tblChegadas.setItems(listaChegadas);
        tblChegadas.setPlaceholder(new Label("Nenhuma chegada para a data/filtros selecionados."));
        tblChegadas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelectionVM) -> {
                Reserva reserva = null;
                if (newSelectionVM != null) {
                    reserva = reservaService.findReservaPorId(newSelectionVM.idProperty().get());
                }
                this.reservaSelecionadaParaCheckIn = reserva;
                mostrarDetalhesCheckInUI(this.reservaSelecionadaParaCheckIn);
            });

        colSaidaHospede.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        colSaidaQuarto.setCellValueFactory(cellData -> cellData.getValue().quartoProperty());
        colSaidaDtCheckOutPrevista.setCellValueFactory(cellData -> cellData.getValue().dataSaidaProperty());
        colSaidaSaldo.setCellValueFactory(cellData -> {
            ReservaViewModel rvm = cellData.getValue();
            if (rvm != null) {
                Reserva r = reservaService.findReservaPorId(rvm.idProperty().get());
                if (r != null) {
                    BigDecimal saldoDevedor = calcularSaldoDevedorReserva(r);
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
                   System.out.println("Listener tblHospedesSaidas: Selecionada Reserva ID: " + 
                                      (novaReservaSelecionada != null ? novaReservaSelecionada.getId() : "null (ViewModel sem Reserva encontrada)"));
               } else {
                   System.out.println("Listener tblHospedesSaidas: Nenhuma ViewModel selecionada (newSelectionVM é null).");
               }
               this.reservaSelecionadaParaCheckOut = novaReservaSelecionada; 
               atualizarPainelDetalhesCheckOutUI(this.reservaSelecionadaParaCheckOut); 
           });
        
        colCOItemConsumo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue())); 
    }

    private void configurarFiltrosEEventos() {
        dateFiltroReferencia.setOnAction(event -> atualizarTodasAsListas());
        btnBuscaGeral.setOnAction(event -> handleBuscarGeral());
        txtBuscaGeral.setOnAction(event -> handleBuscarGeral()); 

        StringConverter<MetodoPagamento> metodoPagamentoConverter = new StringConverter<MetodoPagamento>() {
            @Override
            public String toString(MetodoPagamento metodo) {
                return metodo == null ? "Selecione" : metodo.toString().replace("_", " ");
            }
            @Override
            public MetodoPagamento fromString(String string) {
                if (string == null || string.equals("Selecione")) return null;
                try { return MetodoPagamento.valueOf(string.replace(" ", "_")); } 
                catch (IllegalArgumentException e) { return null; }
            }
        };
        cmbCOMetodoPagamento.setItems(FXCollections.observableArrayList(MetodoPagamento.values()));
        cmbCOMetodoPagamento.setConverter(metodoPagamentoConverter);
        cmbCIPagamentoMetodo.setItems(FXCollections.observableArrayList(MetodoPagamento.values()));
        cmbCIPagamentoMetodo.setConverter(metodoPagamentoConverter);
        
        btnCIRegistrarPagamento.setOnAction(event -> handleRegistrarPagamentoCheckIn());

        cmbCIQuartoDisponivel.setConverter(new StringConverter<Quarto>() {
            @Override
            public String toString(Quarto quarto) {
                return quarto == null ? "Selecione um quarto" : "Nº: " + quarto.getNumeroQuarto() + " (" + quarto.getTipo() + ") - " + Formatter.formatCurrency(quarto.getPrecoDiaria());
            }
            @Override
            public Quarto fromString(String string) { return null; } 
        });
    }
    
    private void configurarPainelDetalhes() {
        limparDetalhesCheckIn();
        limparDetalhesCheckOut();
        if (chkCIDocumentosConfirmados != null) chkCIDocumentosConfirmados.setSelected(false);
        if (chkCOConsumosConfirmados != null) chkCOConsumosConfirmados.setSelected(false);
        
        if (cmbCOItemParaAdicionar != null) {
            cmbCOItemParaAdicionar.getItems().clear();
            cmbCOItemParaAdicionar.setDisable(true);
        }
        if (txtCOConsumoQuantidade != null) {
            txtCOConsumoQuantidade.clear();
            txtCOConsumoQuantidade.setDisable(true);
        }
        if (btnCOAdicionarConsumo != null) {
            btnCOAdicionarConsumo.setDisable(true);
        }
    }

    @FXML
    private void handleBuscarGeral() {
        atualizarTodasAsListas();
    }

    private void atualizarTodasAsListas() {
        LocalDate dataSelecionada = dateFiltroReferencia.getValue();
        String termoBusca = txtBuscaGeral.getText() != null ? txtBuscaGeral.getText().trim() : "";

        if (dataSelecionada == null) {
            listaChegadas.clear();
            listaHospedesSaidas.clear();
            lblChegadasData.setText("Chegadas para: (Selecione uma data)");
            lblHospedesSaidasData.setText("Hóspedes na Casa / Saídas para: (Selecione uma data)");
            tblChegadas.setPlaceholder(new Label("Selecione uma data de referência."));
            tblHospedesSaidas.setPlaceholder(new Label("Selecione uma data de referência."));
            limparDetalhesCheckIn(); 
            limparDetalhesCheckOut();
            return;
        }

        lblChegadasData.setText("Chegadas para: " + Formatter.formatDate(dataSelecionada));
        lblHospedesSaidasData.setText("Hóspedes na Casa / Saídas para: " + Formatter.formatDate(dataSelecionada));

        carregarChegadas(dataSelecionada, termoBusca);
        carregarHospedesSaidas(dataSelecionada, termoBusca);
        
        limparDetalhesCheckIn(); 
        limparDetalhesCheckOut();
        if (tblChegadas != null) tblChegadas.getSelectionModel().clearSelection();
        if (tblHospedesSaidas != null) tblHospedesSaidas.getSelectionModel().clearSelection();
    }

    private void carregarChegadas(LocalDate data, String termoBusca) {
        listaChegadas.clear();
        List<Reserva> chegadasDoDia = new ArrayList<>();
        
        List<Reserva> confirmadas = reservaService.buscarReservasComFiltros(StatusReserva.CONFIRMADA, data, data, termoBusca);
        if (confirmadas != null) chegadasDoDia.addAll(confirmadas);
        
        List<Reserva> pendentes = reservaService.buscarReservasComFiltros(StatusReserva.PENDENTE, data, data, termoBusca);
         if (pendentes != null) {
            pendentes.forEach(p -> {
                if (p.getDataCheckIn() != null && p.getDataCheckIn().equals(data) &&
                    chegadasDoDia.stream().noneMatch(c -> c.getId() == p.getId())) {
                     chegadasDoDia.add(p);
                }
            });
        }
        
        if (!chegadasDoDia.isEmpty()) {
            chegadasDoDia.stream()
                .map(this::toViewModel) 
                .filter(rvm -> rvm != null) 
                .forEach(listaChegadas::add);
        }
        
        tblChegadas.setPlaceholder(new Label(listaChegadas.isEmpty() ? 
            "Nenhuma chegada para " + Formatter.formatDate(data) + (!termoBusca.isEmpty() ? " com o termo '" + termoBusca + "'" : "") + "." 
            : ""));
    }

    private void carregarHospedesSaidas(LocalDate data, String termoBusca) {
        listaHospedesSaidas.clear();
        List<Reserva> ativasOuSaindo = new ArrayList<>();
        
        List<Reserva> hospedados = reservaService.buscarReservasComFiltros(StatusReserva.HOSPEDADO, null, null, termoBusca);
        if (hospedados != null) {
            hospedados.stream()
                .filter(r -> r.getDataCheckIn() != null && r.getDataCheckOut() != null &&
                              !r.getDataCheckIn().isAfter(data) && !r.getDataCheckOut().isBefore(data))
                .forEach(ativasOuSaindo::add);
        }

        List<Reserva> checkoutsPrevistos = reservaService.buscarReservasComFiltros(null, null, data, termoBusca);
        if (checkoutsPrevistos != null) {
            checkoutsPrevistos.stream()
                .filter(r -> r.getDataCheckOut() != null && r.getDataCheckOut().equals(data) &&
                              r.getStatus() != StatusReserva.CONCLUIDA && r.getStatus() != StatusReserva.CANCELADA)
                .filter(r -> ativasOuSaindo.stream().noneMatch(h -> h.getId() == r.getId())) 
                .forEach(ativasOuSaindo::add);
        }

        if (!ativasOuSaindo.isEmpty()) {
             ativasOuSaindo.stream()
                .map(this::toViewModel)
                .filter(rvm -> rvm != null)
                .forEach(listaHospedesSaidas::add);
        }
        
        tblHospedesSaidas.setPlaceholder(new Label(listaHospedesSaidas.isEmpty() ? 
            "Nenhum hóspede/saída para " + Formatter.formatDate(data) + (!termoBusca.isEmpty() ? " com o termo '" + termoBusca + "'" : "") + "."
            : ""));
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

    private BigDecimal calcularTotalPagoParaReserva(int reservaId) {
        if (pagamentoService == null) {
             System.err.println("PagamentoService não inicializado em CheckInController!");
            return BigDecimal.ZERO;
        }
        List<Pagamento> pagamentosDaReserva = pagamentoService.getPagamentosPorReservaId(reservaId); 
        if (pagamentosDaReserva == null || pagamentosDaReserva.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return pagamentosDaReserva.stream()
                .filter(p -> p.getStatus() == StatusPagamento.PAGO) 
                .map(Pagamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularTotalProdutoConsumos(int reservaId) {
        List<Consumo> consumos = produtoConsumoService.findConsumosPorReserva(reservaId);
        if (consumos == null || consumos.isEmpty()) {
            return BigDecimal.ZERO;
        }
        double total = consumos.stream()
            .mapToDouble(c -> c.getValor() * c.getQuantidade())
            .sum();
        return BigDecimal.valueOf(total);
    }
    
    private BigDecimal calcularTotalServicoConsumos(int reservaId) {
        List<ConsumoServicos> consumosServicos = servicoConsumoService.findConsumosPorReserva(reservaId);
        if (consumosServicos == null || consumosServicos.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal totalServicosValor = BigDecimal.ZERO;
        for (ConsumoServicos cs : consumosServicos) {
            Servico servico = servicoService.findServicoPorId(cs.getServicoId());
            if (servico != null && servico.getPreco() != null) {
                BigDecimal precoServico = servico.getPreco();
                totalServicosValor = totalServicosValor.add(precoServico.multiply(BigDecimal.valueOf(cs.getQuantidade())));
            } else {
                System.out.println("WARN: Servico ID " + cs.getServicoId() + " não encontrado ou sem preço. Consumo não contabilizado.");
            }
        }
        return totalServicosValor;
    }

    private BigDecimal calcularSaldoDevedorReserva(Reserva reserva) {
        if (reserva == null) return BigDecimal.ZERO;
        BigDecimal valorReserva = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO;
        BigDecimal totalPago = calcularTotalPagoParaReserva(reserva.getId());
        BigDecimal totalProdutos = calcularTotalProdutoConsumos(reserva.getId());
        BigDecimal totalServicos = calcularTotalServicoConsumos(reserva.getId());

        BigDecimal totalDevido = valorReserva.add(totalProdutos).add(totalServicos);
        return totalDevido.subtract(totalPago);
    }

    private String getStatusPagamentoReservaAggregado(Reserva reserva) {
        if (reserva == null) return "Pendente"; 
        
        BigDecimal valorTotalDevido = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO;
        valorTotalDevido = valorTotalDevido.add(calcularTotalProdutoConsumos(reserva.getId()))
                                           .add(calcularTotalServicoConsumos(reserva.getId()));

        BigDecimal totalPago = calcularTotalPagoParaReserva(reserva.getId());

        if (totalPago.compareTo(BigDecimal.ZERO) <= 0 && valorTotalDevido.compareTo(BigDecimal.ZERO) > 0) {
            return "Pendente";
        } else if (totalPago.compareTo(valorTotalDevido) >= 0) {
            return "Pago"; 
        } else if (totalPago.compareTo(BigDecimal.ZERO) > 0 && totalPago.compareTo(valorTotalDevido) < 0) {
            return "Parcialmente Pago";
        } else { 
            return "N/D"; 
        }
    }

    private void mostrarDetalhesCheckInUI(Reserva reserva) {
        if (reserva == null) {
            limparDetalhesCheckIn();
            return;
        }

        if (reserva.getHospede() != null && reserva.getQuarto() != null) {
            lblCINomeHospede.setText(reserva.getHospede().getNome());
            lblCICPF.setText(Formatter.formatCpf(reserva.getHospede().getCpf()));
            lblCITelefone.setText(reserva.getHospede().getTelefone());
            lblCIEmail.setText(reserva.getHospede().getEmail());

            lblCINumQuarto.setText(String.valueOf(reserva.getQuarto().getNumeroQuarto()));
            lblCITipoQuarto.setText(reserva.getQuarto().getTipo().toString());
            String datas = Formatter.formatDate(reserva.getDataCheckIn()) + " até " + Formatter.formatDate(reserva.getDataCheckOut());
            lblCIDatasEstadia.setText(datas);
            lblCIValorTotalReserva.setText(Formatter.formatCurrency(reserva.getValorTotal()));
            
            if(chkCIDocumentosConfirmados != null) chkCIDocumentosConfirmados.setSelected(false);

            String statusPgtoAggregado = getStatusPagamentoReservaAggregado(reserva);
            lblCIStatusPagamento.setText(statusPgtoAggregado);

            BigDecimal valorReservaBD = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO;
            BigDecimal totalPagoNaReservaBD = calcularTotalPagoParaReserva(reserva.getId());
            BigDecimal saldoPendenteApenasReserva = valorReservaBD.subtract(totalPagoNaReservaBD);
            
            lblCISaldoPendente.setText(Formatter.formatCurrency(saldoPendenteApenasReserva.max(BigDecimal.ZERO)));

            cmbCIQuartoDisponivel.getItems().clear();
            List<Quarto> opcoesDeQuarto = new ArrayList<>();
            opcoesDeQuarto.add(reserva.getQuarto());
            cmbCIQuartoDisponivel.setItems(FXCollections.observableArrayList(opcoesDeQuarto));
            cmbCIQuartoDisponivel.setValue(reserva.getQuarto());
            
            paneDetalhesCheckIn.setVisible(true);
            paneDetalhesCheckIn.setManaged(true);
            
            boolean podeCheckInHoje = reserva.getDataCheckIn().equals(dateFiltroReferencia.getValue());
            boolean statusPermiteCheckIn = (reserva.getStatus() == StatusReserva.CONFIRMADA || reserva.getStatus() == StatusReserva.PENDENTE);
            btnConfirmarCheckIn.setDisable(!(podeCheckInHoje && statusPermiteCheckIn));
            
            boolean pagamentoNecessario = saldoPendenteApenasReserva.compareTo(BigDecimal.ZERO) > 0;
            cmbCIPagamentoMetodo.setDisable(!pagamentoNecessario);
            txtCIPagamentoValor.setDisable(!pagamentoNecessario);
            btnCIRegistrarPagamento.setDisable(!pagamentoNecessario);

            if (pagamentoNecessario) {
                txtCIPagamentoValor.setText(saldoPendenteApenasReserva.max(BigDecimal.ZERO).toString().replace(".", ","));
            } else {
                txtCIPagamentoValor.clear();
            }
        } else {
            limparDetalhesCheckIn();
        }
    }

    private void limparDetalhesCheckIn() {
        paneDetalhesCheckIn.setVisible(false);
        paneDetalhesCheckIn.setManaged(false);
        if (btnConfirmarCheckIn != null) btnConfirmarCheckIn.setDisable(true);
        if (lblCINomeHospede != null) lblCINomeHospede.setText("N/D"); 
        if (lblCICPF != null) lblCICPF.setText("N/D"); 
        if (lblCITelefone != null) lblCITelefone.setText("N/D"); 
        if (lblCIEmail != null) lblCIEmail.setText("N/D");
        if (lblCINumQuarto != null) lblCINumQuarto.setText("N/D"); 
        if (lblCITipoQuarto != null) lblCITipoQuarto.setText("N/D"); 
        if (lblCIDatasEstadia != null) lblCIDatasEstadia.setText("N/D"); 
        if (lblCIValorTotalReserva != null) lblCIValorTotalReserva.setText("N/D"); 
        if (lblCIStatusPagamento != null) lblCIStatusPagamento.setText("N/D");
        if (lblCISaldoPendente != null) lblCISaldoPendente.setText("R$ 0,00");

        if (chkCIDocumentosConfirmados != null) chkCIDocumentosConfirmados.setSelected(false);
        if (cmbCIQuartoDisponivel != null) {
            cmbCIQuartoDisponivel.getItems().clear();
            cmbCIQuartoDisponivel.setDisable(true);
        }
        if (cmbCIPagamentoMetodo != null) {
            cmbCIPagamentoMetodo.getSelectionModel().clearSelection(); 
            cmbCIPagamentoMetodo.setDisable(true);
        }
        if (txtCIPagamentoValor != null) {
            txtCIPagamentoValor.clear(); 
            txtCIPagamentoValor.setDisable(true);
        }
        if (btnCIRegistrarPagamento != null) btnCIRegistrarPagamento.setDisable(true);
        
        System.out.println("limparDetalhesCheckIn: Limpando reservaSelecionadaParaCheckIn. Valor anterior: " + (this.reservaSelecionadaParaCheckIn != null ? "ID " + this.reservaSelecionadaParaCheckIn.getId() : "null"));
        this.reservaSelecionadaParaCheckIn = null;
    }

    @FXML
    private void handleRegistrarPagamentoCheckIn() {
        if (reservaSelecionadaParaCheckIn == null) { 
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para registrar pagamento.");
            return;
        }

        MetodoPagamento metodo = cmbCIPagamentoMetodo.getValue();
        String valorPagoStr = txtCIPagamentoValor.getText();
        BigDecimal valorPago = parseValorPagamento(valorPagoStr); 

        if (metodo == null || valorPago == null) {
             if(metodo == null) mostrarAlerta("Dados Incompletos", "Por favor, selecione o método de pagamento.");
             return;
        }

        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setReservaId(reservaSelecionadaParaCheckIn.getId());
        novoPagamento.setValor(valorPago);
        novoPagamento.setMetodo(metodo);
        novoPagamento.setDataPagamento(LocalDate.now());
        novoPagamento.setStatus(StatusPagamento.PAGO); 

        try {
            if (pagamentoService.addPagamento(novoPagamento)) {
               mostrarAlerta("Sucesso", "Pagamento de " + Formatter.formatCurrency(valorPago) + " registrado para a reserva.");
               mostrarDetalhesCheckInUI(reservaSelecionadaParaCheckIn);
            } else {
               mostrarAlerta("Erro", "Não foi possível registrar o pagamento (retorno do serviço foi false).");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro ao Registrar Pagamento", "Falha ao registrar pagamento: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro inesperado ao registrar o pagamento: " + e.getMessage());
        }
    }


    @FXML
    private void handleConfirmarCheckIn() {
        if (reservaSelecionadaParaCheckIn == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para check-in.");
            return;
        }
        Reserva reserva = reservaSelecionadaParaCheckIn;

        if (chkCIDocumentosConfirmados != null && !chkCIDocumentosConfirmados.isSelected()) {
            mostrarAlerta("Verificação Pendente", "Por favor, confirme os documentos do(s) hóspede(s) antes de prosseguir com o check-in.");
            return;
        }

        LocalDate dataReferencia = dateFiltroReferencia.getValue();
        if (dataReferencia == null || !reserva.getDataCheckIn().equals(dataReferencia)) {
            mostrarAlerta("Aviso de Data", "O check-in deve ser realizado na data de entrada da reserva (" + 
                          Formatter.formatDate(reserva.getDataCheckIn()) + ").\nData de referência atual: " + Formatter.formatDate(dataReferencia));
            return;
        }
        if (reserva.getStatus() != StatusReserva.CONFIRMADA && reserva.getStatus() != StatusReserva.PENDENTE) {
            mostrarAlerta("Status Inválido", "Check-in não permitido para reservas com status: " + reserva.getStatus());
            return;
        }
        
        BigDecimal valorReservaBD = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO;
        BigDecimal totalPagoNaReservaBD = calcularTotalPagoParaReserva(reserva.getId());
        BigDecimal saldoPendenteApenasReserva = valorReservaBD.subtract(totalPagoNaReservaBD);

        if (saldoPendenteApenasReserva.compareTo(BigDecimal.ZERO) > 0) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "A reserva possui um saldo pendente de " + Formatter.formatCurrency(saldoPendenteApenasReserva) + 
                ". Deseja prosseguir com o check-in?",
                ButtonType.YES, ButtonType.NO);
            confirmacao.setTitle("Pagamento Pendente");
            confirmacao.setHeaderText(null);
            Optional<ButtonType> resultado = confirmacao.showAndWait();
            if (resultado.isEmpty() || resultado.get() == ButtonType.NO) {
                return;
            }
        }
        Quarto quartoEscolhidoParaCheckIn = cmbCIQuartoDisponivel.getValue();
        if (quartoEscolhidoParaCheckIn == null) { 
            quartoEscolhidoParaCheckIn = reserva.getQuarto();
        }
        
        Quarto quartoAtualizadoNoSistema = quartoService.findQuartoPorId(quartoEscolhidoParaCheckIn.getId());
        if (quartoAtualizadoNoSistema == null) { 
            mostrarAlerta("Erro", "Quarto selecionado para check-in (Nº " + quartoEscolhidoParaCheckIn.getNumeroQuarto() + ") não encontrado.");
            return;
        }
        boolean quartoFoiTrocado = (quartoAtualizadoNoSistema.getId() != reserva.getQuarto().getId());

        if (quartoAtualizadoNoSistema.getStatus() != StatusQuarto.DISPONIVEL) {
            mostrarAlerta("Quarto Indisponível", "O quarto " + quartoAtualizadoNoSistema.getNumeroQuarto() + 
                          " não está disponível (Status: " + quartoAtualizadoNoSistema.getStatus() + "). Selecione outro quarto.");
            return;
        }

        try {
            if (quartoFoiTrocado) {
                reserva.setQuarto(quartoAtualizadoNoSistema); 
            }
            
            quartoAtualizadoNoSistema.setStatus(StatusQuarto.OCUPADO);
            if (!quartoService.upQuarto(quartoAtualizadoNoSistema)) {
                mostrarAlerta("Erro Crítico", "Falha ao atualizar o status do quarto para OCUPADO.");
                return; 
            }

            reserva.setStatus(StatusReserva.HOSPEDADO);
            
            if (!reservaService.upReserva(reserva)) {
                quartoAtualizadoNoSistema.setStatus(StatusQuarto.DISPONIVEL); 
                quartoService.upQuarto(quartoAtualizadoNoSistema);
                mostrarAlerta("Erro Crítico", "Falha ao atualizar o status da reserva. Status do quarto revertido. Tente novamente.");
                return;
            }

            mostrarAlerta("Sucesso", "Check-in realizado para " + reserva.getHospede().getNome() + 
                          " no quarto " + quartoAtualizadoNoSistema.getNumeroQuarto() + ".");
            
            atualizarTodasAsListas();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro no Check-in", "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    private void popularItensCobrancaComboBox() {
        if (cmbCOItemParaAdicionar == null) return;

        ObservableList<ItemCobrancaViewModel> itensCobraveis = FXCollections.observableArrayList();
        List<Produto> produtos = produtoService.getAllProdutos();
        if (produtos != null) {
            for (Produto p : produtos) {
                itensCobraveis.add(new ItemCobrancaViewModel(p));
            }
        }
        List<Servico> servicos = servicoService.getAllServicos();
        if (servicos != null) {
            for (Servico s : servicos) {
                itensCobraveis.add(new ItemCobrancaViewModel(s));
            }
        }
        cmbCOItemParaAdicionar.setItems(itensCobraveis);
    }

    private void atualizarPainelDetalhesCheckOutUI(Reserva reserva) { 
        if (reserva == null) {
            limparDetalhesCheckOut(); 
            return;
        }
        System.out.println("atualizarPainelDetalhesCheckOutUI: Processando Reserva ID: " + reserva.getId());

        if (reserva.getHospede() != null && reserva.getQuarto() != null) {
            lblCONomeHospede.setText(reserva.getHospede().getNome());
            lblCONumQuarto.setText(String.valueOf(reserva.getQuarto().getNumeroQuarto()));
            String dataRealCheckIn = Formatter.formatDate(reserva.getDataCheckIn());
            String datas = dataRealCheckIn + " até " + Formatter.formatDate(reserva.getDataCheckOut());
            lblCODatasEstadia.setText(datas);

            if(chkCOConsumosConfirmados != null) chkCOConsumosConfirmados.setSelected(false);
            
            boolean podeAdicionarConsumo = (reserva.getStatus() == StatusReserva.HOSPEDADO);
            if (cmbCOItemParaAdicionar != null) {
                cmbCOItemParaAdicionar.setDisable(!podeAdicionarConsumo);
                if (podeAdicionarConsumo) {
                    popularItensCobrancaComboBox();
                } else {
                    cmbCOItemParaAdicionar.getItems().clear();
                }
            }
            if (txtCOConsumoQuantidade != null) txtCOConsumoQuantidade.setDisable(!podeAdicionarConsumo);
            if (btnCOAdicionarConsumo != null) btnCOAdicionarConsumo.setDisable(!podeAdicionarConsumo);


            BigDecimal valorReservaOriginal = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO;
            BigDecimal totalPagoVal = calcularTotalPagoParaReserva(reserva.getId());
            BigDecimal totalProdutosVal = calcularTotalProdutoConsumos(reserva.getId());
            BigDecimal totalServicosVal = calcularTotalServicoConsumos(reserva.getId());
            
            BigDecimal totalConsumosVal = totalProdutosVal.add(totalServicosVal);
            BigDecimal totalContaVal = valorReservaOriginal.add(totalConsumosVal);
            BigDecimal saldoDevedorVal = totalContaVal.subtract(totalPagoVal);

            lblCOTotalReserva.setText(Formatter.formatCurrency(valorReservaOriginal));
            lblCOTotalConsumos.setText(Formatter.formatCurrency(totalConsumosVal));
            lblCOTotalConta.setText(Formatter.formatCurrency(totalContaVal));
            lblCOValorJaPago.setText(Formatter.formatCurrency(totalPagoVal));
            lblCOSaldoDevedor.setText(Formatter.formatCurrency(saldoDevedorVal));

            ObservableList<String> itensContaFormatados = FXCollections.observableArrayList();
            itensContaFormatados.add("Valor da Reserva: " + Formatter.formatCurrency(valorReservaOriginal));
            
            List<Consumo> consumosProdutos = produtoConsumoService.findConsumosPorReserva(reserva.getId());
            if (consumosProdutos != null && !consumosProdutos.isEmpty()) {
                consumosProdutos.forEach(c -> {
                    Produto p = produtoService.findProdutoPorId(c.getIdProduto());
                    String nomeProduto = (p != null && p.getNome() != null) ? p.getNome() : "Produto ID " + c.getIdProduto();
                    itensContaFormatados.add(
                        nomeProduto + " (Qtd: " + c.getQuantidade() + ", Unit: " + Formatter.formatCurrency(c.getValor()) + "): " + 
                        Formatter.formatCurrency(BigDecimal.valueOf(c.getValor()).multiply(BigDecimal.valueOf(c.getQuantidade())))
                    );
                });
            }
            List<ConsumoServicos> consumosServicos = servicoConsumoService.findConsumosPorReserva(reserva.getId());
            if (consumosServicos != null && !consumosServicos.isEmpty()) {
                consumosServicos.forEach(cs -> {
                    Servico s = servicoService.findServicoPorId(cs.getServicoId());
                    String nomeServico = (s != null && s.getNome() != null) ? s.getNome() : "Serviço ID " + cs.getServicoId();
                    BigDecimal precoServico = (s != null && s.getPreco() != null) ? s.getPreco() : BigDecimal.ZERO;
                    BigDecimal valorTotalServico = precoServico.multiply(BigDecimal.valueOf(cs.getQuantidade()));
                    itensContaFormatados.add(nomeServico + " (Qtd: " + cs.getQuantidade() + 
                                            (precoServico.compareTo(BigDecimal.ZERO) > 0 ? ", Unit: " + Formatter.formatCurrency(precoServico) : "") +
                                            "): " + Formatter.formatCurrency(valorTotalServico));
                });
            }

            if ( (consumosProdutos == null || consumosProdutos.isEmpty()) && (consumosServicos == null || consumosServicos.isEmpty()) ){
                 itensContaFormatados.add("Nenhum consumo adicional registrado.");
            }
            tblCOContaConsumo.setItems(itensContaFormatados);

            paneDetalhesCheckOut.setVisible(true);
            paneDetalhesCheckOut.setManaged(true);
            
            boolean podeCheckOut = (reserva.getStatus() == StatusReserva.HOSPEDADO || 
                                   (reserva.getStatus() == StatusReserva.CONFIRMADA && reserva.getDataCheckOut().isBefore(LocalDate.now().plusDays(1)))) &&
                                   (reserva.getDataCheckOut().isBefore(dateFiltroReferencia.getValue().plusDays(1))); 

            btnCOConfirmarCheckOut.setDisable(!podeCheckOut);
            boolean registrarPagamentoHabilitado = podeCheckOut && saldoDevedorVal.compareTo(BigDecimal.ZERO) > 0;
            btnCORegistrarPagamento.setDisable(!registrarPagamentoHabilitado);
            txtCOValorPago.setDisable(!registrarPagamentoHabilitado);
            cmbCOMetodoPagamento.setDisable(!registrarPagamentoHabilitado);
            
            if(reserva.getStatus() == StatusReserva.CONCLUIDA || reserva.getStatus() == StatusReserva.CANCELADA){
                 btnCOConfirmarCheckOut.setDisable(true);
                 btnCORegistrarPagamento.setDisable(true);
                 txtCOValorPago.setDisable(true);
                 cmbCOMetodoPagamento.setDisable(true);
                 if (chkCOConsumosConfirmados != null) chkCOConsumosConfirmados.setDisable(true);
                 if (cmbCOItemParaAdicionar != null) cmbCOItemParaAdicionar.setDisable(true);
                 if (txtCOConsumoQuantidade != null) txtCOConsumoQuantidade.setDisable(true);
                 if (btnCOAdicionarConsumo != null) btnCOAdicionarConsumo.setDisable(true);
            } else {
                if (chkCOConsumosConfirmados != null) chkCOConsumosConfirmados.setDisable(!podeCheckOut);
                if (registrarPagamentoHabilitado) {
                    txtCOValorPago.setText(saldoDevedorVal.toString().replace(".",","));
                } else {
                    txtCOValorPago.clear();
                }
            }
        }  else {
             limparDetalhesCheckOut();
        }
    }
    
    private void limparDetalhesCheckOut() {
        paneDetalhesCheckOut.setVisible(false);
        paneDetalhesCheckOut.setManaged(false);
        if (btnCOConfirmarCheckOut != null) btnCOConfirmarCheckOut.setDisable(true);
        if (btnCORegistrarPagamento != null) btnCORegistrarPagamento.setDisable(true);
        if (lblCONomeHospede != null) lblCONomeHospede.setText("N/D"); 
        if (lblCONumQuarto != null) lblCONumQuarto.setText("N/D"); 
        if (lblCODatasEstadia != null) lblCODatasEstadia.setText("N/D");
        if (lblCOTotalReserva != null) lblCOTotalReserva.setText("R$ 0,00"); 
        if (lblCOTotalConsumos != null) lblCOTotalConsumos.setText("R$ 0,00");
        if (lblCOTotalConta != null) lblCOTotalConta.setText("R$ 0,00"); 
        if (lblCOValorJaPago != null) lblCOValorJaPago.setText("R$ 0,00"); 
        if (lblCOSaldoDevedor != null) lblCOSaldoDevedor.setText("R$ 0,00");
        
        if (chkCOConsumosConfirmados != null) chkCOConsumosConfirmados.setSelected(false);
        if (cmbCOItemParaAdicionar != null) {
            cmbCOItemParaAdicionar.getItems().clear();
            cmbCOItemParaAdicionar.setDisable(true);
        }
        if (txtCOConsumoQuantidade != null) {
            txtCOConsumoQuantidade.clear();
            txtCOConsumoQuantidade.setDisable(true);
        }
        if (btnCOAdicionarConsumo != null) btnCOAdicionarConsumo.setDisable(true);

        if (tblCOContaConsumo != null) tblCOContaConsumo.getItems().clear();
        if (cmbCOMetodoPagamento != null) {
            cmbCOMetodoPagamento.getSelectionModel().clearSelection(); 
            cmbCOMetodoPagamento.setDisable(true);
        }
        if (txtCOValorPago != null) {
            txtCOValorPago.clear(); 
            txtCOValorPago.setDisable(true);
        }
        
        System.out.println("limparDetalhesCheckOut: Limpando reservaSelecionadaParaCheckOut. Valor anterior: " + (this.reservaSelecionadaParaCheckOut != null ? "ID " + this.reservaSelecionadaParaCheckOut.getId() : "null"));
        this.reservaSelecionadaParaCheckOut = null;
    }

    @FXML
    private void handleAdicionarConsumo() {
        if (reservaSelecionadaParaCheckOut == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para adicionar consumo.");
            return;
        }
        ItemCobrancaViewModel itemSelecionado = cmbCOItemParaAdicionar.getValue();
        if (itemSelecionado == null) {
            mostrarAlerta("Item não selecionado", "Por favor, selecione um produto ou serviço para adicionar.");
            return;
        }
        String qtdStr = txtCOConsumoQuantidade.getText();
        if (qtdStr == null || qtdStr.trim().isEmpty()) {
            mostrarAlerta("Quantidade Inválida", "Por favor, informe a quantidade.");
            return;
        }
        int quantidade;
        try {
            quantidade = Integer.parseInt(qtdStr.trim());
            if (quantidade <= 0) {
                mostrarAlerta("Quantidade Inválida", "A quantidade deve ser maior que zero.");
                return;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Quantidade Inválida", "Por favor, insira um número válido para a quantidade.");
            return;
        }

        boolean sucesso = false;
        String nomeItemAdicionado = "";

        try {
            if (itemSelecionado.isProduto()) {
                Produto produto = (Produto) itemSelecionado.getItem();
                nomeItemAdicionado = produto.getNome();
                
                Consumo novoConsumoProduto = new Consumo();
                novoConsumoProduto.setIdReserva(reservaSelecionadaParaCheckOut.getId());
                novoConsumoProduto.setIdProduto(produto.getId());
                novoConsumoProduto.setQuantidade(quantidade);
                novoConsumoProduto.setValor(produto.getPreco().doubleValue()); 
                novoConsumoProduto.setDataConsumo(LocalDate.now());
                
                sucesso = produtoConsumoService.addConsumo(novoConsumoProduto);

            } else { 
                Servico servico = (Servico) itemSelecionado.getItem();
                nomeItemAdicionado = servico.getNome();

                ConsumoServicos novoConsumoServico = new ConsumoServicos(0,0,0,0,null,null,null);
                novoConsumoServico.setReservaId(reservaSelecionadaParaCheckOut.getId());
                novoConsumoServico.setServicoId(servico.getId());
                novoConsumoServico.setQuantidade(quantidade);
                novoConsumoServico.setDataConsumo(LocalDate.now());
                
                sucesso = servicoConsumoService.addConsumo(novoConsumoServico);
            }

            if (sucesso) {
                mostrarAlerta("Sucesso", "Consumo de " + quantidade + "x " + nomeItemAdicionado + " adicionado.");
                txtCOConsumoQuantidade.clear();
                cmbCOItemParaAdicionar.getSelectionModel().clearSelection();
                atualizarPainelDetalhesCheckOutUI(reservaSelecionadaParaCheckOut);
            } else {
                mostrarAlerta("Erro", "Não foi possível adicionar o consumo. Verifique os logs para mais detalhes.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro ao adicionar consumo: " + e.getMessage());
        }
    }


    @FXML
    private void handleConfirmarCheckOut() {
        if (reservaSelecionadaParaCheckOut == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para check-out.");
            return;
        }
        Reserva reserva = reservaSelecionadaParaCheckOut;

        if (chkCOConsumosConfirmados != null && !chkCOConsumosConfirmados.isSelected()) {
            mostrarAlerta("Verificação Pendente", "Por favor, confirme todos os consumos (frigobar, serviços, etc.) antes de prosseguir com o check-out.");
            return;
        }
        
        BigDecimal saldoDevedorVal = calcularSaldoDevedorReserva(reserva);

        if (saldoDevedorVal.compareTo(BigDecimal.ZERO) > 0) {
            mostrarAlerta("Pagamento Pendente", "Não é possível realizar o check-out. Existe um saldo devedor de " 
                          + Formatter.formatCurrency(saldoDevedorVal) + ".\nPor favor, registre o pagamento antes de continuar.");
            return;
        }

        try {
            Quarto quarto = quartoService.findQuartoPorId(reserva.getQuarto().getId());
            if (quarto == null) {
                mostrarAlerta("Erro", "Quarto da reserva não encontrado.");
                return;
            }

            quarto.setStatus(StatusQuarto.DISPONIVEL); 
            if(!quartoService.upQuarto(quarto)){
                mostrarAlerta("Erro Crítico", "Falha ao atualizar o status do quarto para disponível.");
                return;
            }

            reserva.setStatus(StatusReserva.CONCLUIDA);
            
            if(!reservaService.upReserva(reserva)){
                quarto.setStatus(StatusQuarto.OCUPADO); 
                quartoService.upQuarto(quarto);
                mostrarAlerta("Erro Crítico", "Falha ao concluir a reserva. Status do quarto revertido. Tente novamente.");
                return;
            }

            mostrarAlerta("Sucesso", "Check-out realizado para " + reserva.getHospede().getNome() + 
                          " do quarto " + quarto.getNumeroQuarto() + ".");
            
            atualizarTodasAsListas();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro no Check-out", "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegistrarPagamentoFinal() { 
        System.out.println("handleRegistrarPagamentoFinal: Iniciado. reservaSelecionadaParaCheckOut é: " + 
                           (reservaSelecionadaParaCheckOut != null ? "ID " + reservaSelecionadaParaCheckOut.getId() : "null"));
        if (reservaSelecionadaParaCheckOut == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para registrar pagamento.");
            return;
        }
        Reserva reserva = reservaSelecionadaParaCheckOut;

        MetodoPagamento metodo = cmbCOMetodoPagamento.getValue();
        String valorPagoStr = txtCOValorPago.getText();
        BigDecimal valorPago = parseValorPagamento(valorPagoStr);

        if (metodo == null || valorPago == null) {
            if(metodo == null) mostrarAlerta("Dados Incompletos", "Por favor, selecione o método de pagamento.");
            return;
        }

        Pagamento novoPagamento = new Pagamento();
        novoPagamento.setReservaId(reserva.getId());
        novoPagamento.setValor(valorPago);
        novoPagamento.setMetodo(metodo);
        novoPagamento.setDataPagamento(LocalDate.now()); 
        novoPagamento.setStatus(StatusPagamento.PAGO);
        
        try {
            if (pagamentoService.addPagamento(novoPagamento)) {
               mostrarAlerta("Sucesso", "Pagamento de " + Formatter.formatCurrency(valorPago) + " registrado.");
               atualizarPainelDetalhesCheckOutUI(reserva);
            } else {
               mostrarAlerta("Erro", "Não foi possível registrar o pagamento (serviço retornou false).");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro ao Registrar Pagamento", "Falha ao registrar pagamento: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro inesperado ao registrar o pagamento: " + e.getMessage());
        }
    }
    
    private BigDecimal parseValorPagamento(String valorStr) {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            mostrarAlerta("Valor Inválido", "O valor do pagamento não pode estar vazio.");
            return null;
        }
        BigDecimal valor = null;
        try {
            DecimalFormatSymbols symbolsComma = new DecimalFormatSymbols();
            symbolsComma.setDecimalSeparator(',');
            symbolsComma.setGroupingSeparator('.');
            DecimalFormat dfComma = new DecimalFormat("#,##0.00", symbolsComma); 
            dfComma.setParseBigDecimal(true);
            valor = (BigDecimal) dfComma.parse(valorStr.trim().replace(".", "")); 
        } catch (java.text.ParseException e) {
            try {
                DecimalFormatSymbols symbolsDot = new DecimalFormatSymbols();
                symbolsDot.setDecimalSeparator('.');
                symbolsDot.setGroupingSeparator(',');
                DecimalFormat dfDot = new DecimalFormat("#,##0.00", symbolsDot); 
                dfDot.setParseBigDecimal(true);
                valor = (BigDecimal) dfDot.parse(valorStr.trim().replace(",", ""));  
            } catch (java.text.ParseException ex) {
                 try { 
                    valor = new BigDecimal(valorStr.trim().replace(',', '.'));
                 } catch (NumberFormatException nfe){
                    mostrarAlerta("Valor Inválido", "Formato numérico inválido para o pagamento (ex: 150,50 ou 150.50).");
                    return null;
                 }
            }
        }
        
        if (valor != null && valor.compareTo(BigDecimal.ZERO) <= 0) {
            mostrarAlerta("Valor Inválido", "O valor do pagamento deve ser positivo.");
            return null;
        }
        return valor;
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert.AlertType tipo = Alert.AlertType.INFORMATION;
        String tituloLower = titulo.toLowerCase();
        if (tituloLower.contains("erro") || tituloLower.contains("falha") || tituloLower.contains("conflito")) {
            tipo = Alert.AlertType.ERROR;
        } else if (tituloLower.contains("aviso") || tituloLower.contains("atenção") || tituloLower.contains("pendente") || tituloLower.contains("inválida")) {
            tipo = Alert.AlertType.WARNING;
        } else if (tituloLower.contains("sucesso") || tituloLower.contains("realizado")) {
             tipo = Alert.AlertType.INFORMATION;
        }
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null); 
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}