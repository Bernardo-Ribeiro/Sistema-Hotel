package com.hotel.gerenciador.controllers;

import com.hotel.gerenciador.models.*;
import com.hotel.gerenciador.services.*;
import com.hotel.gerenciador.utils.*;
import com.hotel.gerenciador.viewmodels.ReservaViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.*;

public class CheckInTabController extends BaseController {
    @FXML private TableView<ReservaViewModel> tblChegadas;
    @FXML private TableColumn<ReservaViewModel, Integer> colChegadaIdReserva;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaHospede;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaQuarto;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaDtCheckIn;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaDtCheckOut;
    @FXML private TableColumn<ReservaViewModel, String> colChegadaStatusReserva;
    
    @FXML private VBox paneDetalhesCheckIn;
    @FXML private Label lblCINomeHospede, lblCICPF, lblCITelefone, lblCIEmail;
    @FXML private Label lblCINumQuarto, lblCITipoQuarto, lblCIDatasEstadia;
    @FXML private Label lblCIValorTotalReserva, lblCIStatusPagamento;
    @FXML private ComboBox<Quarto> cmbCIQuartoDisponivel;
    @FXML private CheckBox chkCIDocumentosConfirmados;
    @FXML private Button btnConfirmarCheckIn;
    @FXML private ComboBox<MetodoPagamento> cmbCIPagamentoMetodo;
    @FXML private TextField txtCIPagamentoValor;
    @FXML private Button btnCIRegistrarPagamento;
    @FXML private Label lblCISaldoPendente;

    private final ReservaService reservaService;
    private final QuartoService quartoService;
    private final PagamentoService pagamentoService;
    private final ConsumoService consumoService;
    private final ConsumoServicosService consumoServicosService;
    private final CalculadoraFinanceira calculadoraFinanceira;
    private final ObservableList<ReservaViewModel> listaChegadas;
    private Reserva reservaSelecionada;

    public CheckInTabController() {
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        this.pagamentoService = new PagamentoService();
        this.consumoService = new ConsumoService();
        this.consumoServicosService = new ConsumoServicosService();
        this.calculadoraFinanceira = new CalculadoraFinanceira(
            pagamentoService, consumoService, consumoServicosService);
        this.listaChegadas = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        configurarTabela();
        configurarComboBoxes();
        configurarEventos();
    }

    private void configurarTabela() {
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
                this.reservaSelecionada = reserva;
                mostrarDetalhesCheckIn(reserva);
            });
    }

    private void configurarComboBoxes() {
        // Configurar ComboBox de Quartos
        cmbCIQuartoDisponivel.setConverter(new StringConverter<Quarto>() {
            @Override
            public String toString(Quarto quarto) {
                return quarto == null ? "Selecione um quarto" : 
                    String.format("Nº: %d (%s) - %s", 
                        quarto.getNumeroQuarto(), 
                        quarto.getTipo(), 
                        Formatter.formatCurrency(quarto.getPrecoDiaria()));
            }
            @Override
            public Quarto fromString(String string) { return null; }
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
        
        cmbCIPagamentoMetodo.setItems(FXCollections.observableArrayList(MetodoPagamento.values()));
        cmbCIPagamentoMetodo.setConverter(metodoPagamentoConverter);
    }

    private void configurarEventos() {
        btnCIRegistrarPagamento.setOnAction(event -> handleRegistrarPagamento());
        btnConfirmarCheckIn.setOnAction(event -> handleConfirmarCheckIn());
    }

    @FXML
    private void handleRegistrarPagamento() {
        try {
            Validator.validateNotNull(reservaSelecionada, "Reserva");
            Validator.validateNotNull(cmbCIPagamentoMetodo.getValue(), "Método de pagamento");

            String valorStr = txtCIPagamentoValor.getText();
            BigDecimal valor = parseValorPagamento(valorStr);
            Validator.validatePositiveValue(valor);

            Pagamento pagamento = new Pagamento();
            pagamento.setReservaId(reservaSelecionada.getId());
            pagamento.setValor(valor);
            pagamento.setMetodo(cmbCIPagamentoMetodo.getValue());
            pagamento.setStatus(StatusPagamento.PAGO);
            pagamento.setDataPagamento(LocalDate.now());

            if (pagamentoService.addPagamento(pagamento)) {
                mostrarAlerta("Sucesso", "Pagamento registrado com sucesso!");
                mostrarDetalhesCheckIn(reservaSelecionada);
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
    private void handleConfirmarCheckIn() {
        try {
            Validator.validateNotNull(reservaSelecionada, "Reserva");
            Validator.validateTrue(chkCIDocumentosConfirmados.isSelected(), 
                "Confirme a verificação dos documentos");

            Quarto quartoSelecionado = cmbCIQuartoDisponivel.getValue();
            // Se nenhum quarto foi selecionado (manter atual), usa o quarto original da reserva
            if (quartoSelecionado == null) {
                quartoSelecionado = reservaSelecionada.getQuarto();
            }
            
            Validator.validateNotNull(quartoSelecionado, "Quarto");
            Validator.validateTrue(quartoSelecionado.getStatus() == StatusQuarto.DISPONIVEL,
                "O quarto selecionado não está disponível");

            // Atualizar status da reserva
            reservaSelecionada.setStatus(StatusReserva.HOSPEDADO);
            reservaSelecionada.setQuarto(quartoSelecionado);
            
            // Atualizar status do quarto
            quartoSelecionado.setStatus(StatusQuarto.OCUPADO);

            if (reservaService.upReserva(reservaSelecionada) && 
                quartoService.upQuarto(quartoSelecionado)) {
                mostrarAlerta("Sucesso", "Check-in realizado com sucesso!");
                limparDetalhesCheckIn();
                atualizarListaChegadas();
            } else {
                throw new Exception("Não foi possível realizar o check-in.");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro ao realizar o check-in: " + e.getMessage());
        }
    }

    private void mostrarDetalhesCheckIn(Reserva reserva) {
        if (reserva == null) {
            limparDetalhesCheckIn();
            return;
        }

        // Tornar o painel visível
        paneDetalhesCheckIn.setVisible(true);
        paneDetalhesCheckIn.setManaged(true);

        // Preencher dados do hóspede
        lblCINomeHospede.setText(reserva.getHospede().getNome());
        lblCICPF.setText(Formatter.formatCpf(reserva.getHospede().getCpf()));
        lblCITelefone.setText(Formatter.formatPhone(reserva.getHospede().getTelefone()));
        lblCIEmail.setText(reserva.getHospede().getEmail());

        // Preencher dados da reserva
        lblCIDatasEstadia.setText(String.format("%s a %s",
            Formatter.formatDate(reserva.getDataCheckIn()),
            Formatter.formatDate(reserva.getDataCheckOut())));
        lblCIValorTotalReserva.setText(Formatter.formatCurrency(reserva.getValorTotal()));
        
        // Atualizar saldo pendente
        BigDecimal saldoDevedor = calculadoraFinanceira.calcularSaldoDevedorReserva(reserva);
        lblCISaldoPendente.setText(Formatter.formatCurrency(saldoDevedor));
        
        // Atualizar status do pagamento
        lblCIStatusPagamento.setText(getStatusPagamentoReservaAggregado(reserva));

        // Carregar quartos disponíveis
        List<Quarto> quartosDisponiveis = quartoService.findAll().stream()
            .filter(q -> q.getStatus() == StatusQuarto.DISPONIVEL)
            .toList();
        cmbCIQuartoDisponivel.setItems(FXCollections.observableArrayList(quartosDisponiveis));
        
        // Habilitar/desabilitar controles
        boolean podeFazerCheckIn = reserva.getStatus() == StatusReserva.CONFIRMADA;
        btnConfirmarCheckIn.setDisable(!podeFazerCheckIn);
        cmbCIQuartoDisponivel.setDisable(!podeFazerCheckIn);
        chkCIDocumentosConfirmados.setDisable(!podeFazerCheckIn);
    }

    private void limparDetalhesCheckIn() {
        lblCINomeHospede.setText("");
        lblCICPF.setText("");
        lblCITelefone.setText("");
        lblCIEmail.setText("");
        lblCIDatasEstadia.setText("");
        lblCIValorTotalReserva.setText("");
        lblCISaldoPendente.setText("");
        lblCIStatusPagamento.setText("");
        
        cmbCIQuartoDisponivel.getItems().clear();
        chkCIDocumentosConfirmados.setSelected(false);
        txtCIPagamentoValor.clear();
        cmbCIPagamentoMetodo.setValue(null);
        
        btnConfirmarCheckIn.setDisable(true);
        cmbCIQuartoDisponivel.setDisable(true);
        chkCIDocumentosConfirmados.setDisable(true);
        
        // Esconder o painel
        paneDetalhesCheckIn.setVisible(false);
        paneDetalhesCheckIn.setManaged(false);
    }

    private void atualizarListaChegadas() {
        LocalDate dataReferencia = LocalDate.now();
        carregarChegadas(dataReferencia, "");
    }

    public void carregarChegadas(LocalDate data, String termoBusca) {
        List<Reserva> reservas = reservaService.buscarReservasComFiltros(StatusReserva.CONFIRMADA, data, data, termoBusca);
        listaChegadas.clear();
        listaChegadas.addAll(reservas.stream()
            .map(this::toViewModel)
            .toList());
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

    private String getStatusPagamentoReservaAggregado(Reserva reserva) {
        BigDecimal valorTotal = reserva.getValorTotal();
        BigDecimal totalPago = calculadoraFinanceira.calcularTotalPagoParaReserva(reserva.getId());
        
        if (totalPago.compareTo(BigDecimal.ZERO) == 0) {
            return "Nenhum pagamento registrado";
        } else if (totalPago.compareTo(valorTotal) >= 0) {
            return "Pagamento completo";
        } else {
            return String.format("Pago parcialmente: %s de %s",
                Formatter.formatCurrency(totalPago),
                Formatter.formatCurrency(valorTotal));
        }
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