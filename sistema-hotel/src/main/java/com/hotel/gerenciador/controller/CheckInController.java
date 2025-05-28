package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Hospede;
import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.viewmodel.ReservaViewModel;
import com.hotel.gerenciador.service.HospedeService;
import com.hotel.gerenciador.service.QuartoService;
import com.hotel.gerenciador.service.ReservaService;
// import com.hotel.gerenciador.service.PagamentoService; // Para funcionalidades de pagamento
// import com.hotel.gerenciador.service.ConsumoService;   // Para funcionalidades de consumo
import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.MetodoPagamento;
import com.hotel.gerenciador.util.StatusQuarto;
import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.TipoQuarto;

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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CheckInController {

    // == Campos FXML ==
    @FXML private DatePicker dateFiltroReferencia;
    @FXML private TextField txtBuscaGeral;
    @FXML private Button btnBuscaGeral;

    @FXML private TabPane tabPanePrincipal;
    @FXML private Tab tabCheckIn;
    @FXML private Tab tabCheckOut;

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
    @FXML private Label lblCINumQuarto, lblCITipoQuarto, lblCIDatasEstadia, lblCIValorTotal, lblCIStatusPagamento;
    @FXML private ComboBox<Quarto> cmbCIQuartoDisponivel;
    @FXML private TextArea txtCIObservacoes;
    @FXML private Button btnConfirmarCheckIn;

    @FXML private Label lblHospedesSaidasData;
    @FXML private TableView<ReservaViewModel> tblHospedesSaidas;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaHospede;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaQuarto;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaDtCheckOutPrevista;
    @FXML private TableColumn<ReservaViewModel, String> colSaidaSaldo;

    @FXML private VBox paneDetalhesCheckOut;
    @FXML private Label lblCONomeHospede, lblCONumQuarto, lblCODatasEstadia;
    @FXML private TableView<String> tblCOContaConsumo; 
    @FXML private TableColumn<String, String> colCOItemConsumo;
    @FXML private TableColumn<String, String> colCOValorConsumo;
    @FXML private Label lblCOTotalConta, lblCOValorJaPago, lblCOSaldoDevedor;
    @FXML private ComboBox<MetodoPagamento> cmbCOMetodoPagamento;
    @FXML private TextField txtCOValorPago;
    @FXML private Button btnCORegistrarPagamento;
    @FXML private Button btnCOConfirmarCheckOut;

    private ReservaService reservaService;
    private QuartoService quartoService;
    private HospedeService hospedeService; 
    // private PagamentoService pagamentoService;
    // private ConsumoService consumoService;

    private ObservableList<ReservaViewModel> listaChegadas = FXCollections.observableArrayList();
    private ObservableList<ReservaViewModel> listaHospedesSaidas = FXCollections.observableArrayList();

    public CheckInController() {
        this.reservaService = new ReservaService();
        this.quartoService = new QuartoService();
        this.hospedeService = new HospedeService(); 
    }

    @FXML
    private void initialize() {
        configurarTabelas();
        configurarFiltrosEEventos();
        configurarPainelDetalhes();
        
        dateFiltroReferencia.setValue(LocalDate.now());
        atualizarTodasAsListas();
    }

    private void configurarTabelas() {
        // Configuração da Tabela de Chegadas
        colChegadaIdReserva.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colChegadaHospede.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        colChegadaQuarto.setCellValueFactory(cellData -> cellData.getValue().quartoProperty());
        colChegadaDtCheckIn.setCellValueFactory(cellData -> cellData.getValue().dataEntradaProperty());
        colChegadaDtCheckOut.setCellValueFactory(cellData -> cellData.getValue().dataSaidaProperty());
        colChegadaStatusReserva.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
        tblChegadas.setItems(listaChegadas);
        tblChegadas.setPlaceholder(new Label("Nenhuma chegada para a data/filtros selecionados."));
        tblChegadas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> mostrarDetalhesCheckIn(newSelection));

        // Configuração da Tabela de Hóspedes/Saídas
        colSaidaHospede.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        colSaidaQuarto.setCellValueFactory(cellData -> cellData.getValue().quartoProperty());
        colSaidaDtCheckOutPrevista.setCellValueFactory(cellData -> cellData.getValue().dataSaidaProperty());
        colSaidaSaldo.setCellValueFactory(cellData -> {
            // TODO: Implementar cálculo real de saldo com PagamentoService e ConsumoService
            // Por enquanto, um placeholder.
            ReservaViewModel rvm = cellData.getValue();
            if (rvm != null && rvm.valorTotalProperty() != null && !rvm.valorTotalProperty().get().isEmpty() &&
                !rvm.valorTotalProperty().get().equals(Formatter.formatCurrency(BigDecimal.ZERO))) {
                return new SimpleStringProperty(rvm.valorTotalProperty().get() + " (a pagar)");
            }
            return new SimpleStringProperty("Pago/Verificar");
        });
        tblHospedesSaidas.setItems(listaHospedesSaidas);
        tblHospedesSaidas.setPlaceholder(new Label("Nenhum hóspede na casa ou saída para a data/filtros."));
        tblHospedesSaidas.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> mostrarDetalhesCheckOut(newSelection));
        
        // Configuração básica da tabela de consumos (check-out)
        colCOItemConsumo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue())); 
        colCOValorConsumo.setCellValueFactory(cellData -> new SimpleStringProperty("")); 
    }

    private void configurarFiltrosEEventos() {
        dateFiltroReferencia.setOnAction(event -> atualizarTodasAsListas());
        btnBuscaGeral.setOnAction(event -> handleBuscarGeral());
        txtBuscaGeral.setOnAction(event -> handleBuscarGeral()); 

        cmbCOMetodoPagamento.setItems(FXCollections.observableArrayList(MetodoPagamento.values()));
        cmbCOMetodoPagamento.setConverter(new StringConverter<MetodoPagamento>() { // Adicionado StringConverter
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
        });
         cmbCIQuartoDisponivel.setConverter(new StringConverter<Quarto>() {
            @Override
            public String toString(Quarto quarto) {
                return quarto == null ? "Selecione um quarto" : "Nº: " + quarto.getNumeroQuarto() + " (" + quarto.getTipo() + ") - " + Formatter.formatCurrency(quarto.getPrecoDiaria());
            }
            @Override
            public Quarto fromString(String string) { return null; } // Não usado para ComboBox não editável
        });
    }
    
    private void configurarPainelDetalhes() {
        limparDetalhesCheckIn();
        limparDetalhesCheckOut();
    }

    @FXML
    private void handleBuscarGeral() {
        atualizarTodasAsListas();
    }

    private void atualizarTodasAsListas() {
        LocalDate dataSelecionada = dateFiltroReferencia.getValue();
        String termoBusca = txtBuscaGeral.getText() != null ? txtBuscaGeral.getText().trim() : null;

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
    }

    private void carregarChegadas(LocalDate data, String termoBusca) {
        listaChegadas.clear();
        List<Reserva> chegadasDoDia = new ArrayList<>();
        
        List<Reserva> confirmadas = reservaService.buscarReservasComFiltros(StatusReserva.CONFIRMADA, data, data, termoBusca);
        if (confirmadas != null) chegadasDoDia.addAll(confirmadas);
        
        List<Reserva> pendentes = reservaService.buscarReservasComFiltros(StatusReserva.PENDENTE, data, data, termoBusca);
        if (pendentes != null) {
            pendentes.forEach(p -> {
                if (chegadasDoDia.stream().noneMatch(c -> c.getId() == p.getId())) {
                    chegadasDoDia.add(p);
                }
            });
        }
        

        if (!chegadasDoDia.isEmpty()) {
            chegadasDoDia.forEach(r -> listaChegadas.add(toViewModel(r)));
        }
        
        tblChegadas.setPlaceholder(new Label(listaChegadas.isEmpty() ? 
            "Nenhuma chegada para " + Formatter.formatDate(data) + (termoBusca != null && !termoBusca.isEmpty() ? " com o termo '" + termoBusca + "'" : "") + "." 
            : ""));
    }

    private void carregarHospedesSaidas(LocalDate data, String termoBusca) {
        listaHospedesSaidas.clear();
        // TODO: Implementar no ReservaService/DAO: findReservasAtivasNaDataOuSaindoNaData(data, termoBusca)
        // Por enquanto, filtragem em memória (menos eficiente)
        List<Reserva> todasAtivasBruto = reservaService.buscarReservasComFiltros(null, data.minusMonths(1), data.plusMonths(1), termoBusca); // Range amplo
        
        if(todasAtivasBruto != null) {
            List<Reserva> filtradas = todasAtivasBruto.stream()
                .filter(r -> r.getQuarto() != null && r.getHospede() != null &&
                             r.getDataCheckIn() != null && r.getDataCheckOut() != null &&
                             (r.getStatus() == StatusReserva.CONFIRMADA /*|| r.getStatus() == StatusReserva.HOSPEDADO*/ )) // Se tiver HOSPEDADO
                .filter(r -> (!r.getDataCheckIn().isAfter(data) && !r.getDataCheckOut().isBefore(data)) ) // Está na casa
                .collect(Collectors.toList());
            
            filtradas.forEach(r -> listaHospedesSaidas.add(toViewModel(r)));
        }
        
        tblHospedesSaidas.setPlaceholder(new Label(listaHospedesSaidas.isEmpty() ? 
            "Nenhum hóspede/saída para " + Formatter.formatDate(data) + (termoBusca != null && !termoBusca.isEmpty() ? " com o termo '" + termoBusca + "'" : "") + "."
            : ""));
    }
    
    private ReservaViewModel toViewModel(Reserva reserva) {
        if (reserva == null) return null;
        String nomeHospede = (reserva.getHospede() != null && reserva.getHospede().getNome() != null) ? reserva.getHospede().getNome() : "N/D";
        String numeroQuarto = (reserva.getQuarto() != null) ? String.valueOf(reserva.getQuarto().getNumeroQuarto()) : "N/D";
        String dataCheckInFormatada = (reserva.getDataCheckIn() != null) ? Formatter.formatDate(reserva.getDataCheckIn()) : "";
        String dataCheckOutFormatada = (reserva.getDataCheckOut() != null) ? Formatter.formatDate(reserva.getDataCheckOut()) : "";
        String valorTotalStr = (reserva.getValorTotal() != null) ? Formatter.formatCurrency(reserva.getValorTotal()) : Formatter.formatCurrency(BigDecimal.ZERO);
        StringProperty valorTotalProperty = new SimpleStringProperty(valorTotalStr); 
        String statusReserva = (reserva.getStatus() != null) ? reserva.getStatus().name() : "";

        return new ReservaViewModel(
            reserva.getId(), nomeHospede, numeroQuarto,
            dataCheckInFormatada, dataCheckOutFormatada, statusReserva,
            valorTotalProperty
        );
    }

    private void mostrarDetalhesCheckIn(ReservaViewModel rvm) {
        if (rvm == null) {
            limparDetalhesCheckIn();
            return;
        }
        Reserva reserva = reservaService.findReservaPorId(rvm.idProperty().get()); 
        if (reserva != null && reserva.getHospede() != null && reserva.getQuarto() != null) {
            lblCINomeHospede.setText(reserva.getHospede().getNome());
            lblCICPF.setText(Formatter.formatCpf(reserva.getHospede().getCpf()));
            lblCITelefone.setText(reserva.getHospede().getTelefone());
            lblCIEmail.setText(reserva.getHospede().getEmail());

            lblCINumQuarto.setText(String.valueOf(reserva.getQuarto().getNumeroQuarto()));
            lblCITipoQuarto.setText(reserva.getQuarto().getTipo().toString());
            String datas = Formatter.formatDate(reserva.getDataCheckIn()) + " até " + Formatter.formatDate(reserva.getDataCheckOut());
            lblCIDatasEstadia.setText(datas);
            lblCIValorTotal.setText(Formatter.formatCurrency(reserva.getValorTotal()));
            
            lblCIStatusPagamento.setText("Verificar Pagamento"); 

            cmbCIQuartoDisponivel.getItems().clear();
            cmbCIQuartoDisponivel.setDisable(false);
            List<Quarto> opcoesDeQuarto = new ArrayList<>();
            opcoesDeQuarto.add(reserva.getQuarto());

            List<Quarto> outrosQuartosDoTipo = quartoService.findByTipo(reserva.getQuarto().getTipo());
            if (outrosQuartosDoTipo != null) {
                for (Quarto q : outrosQuartosDoTipo) {
                    if (q.getId() != reserva.getQuarto().getId()) {
                        try {
                            if (reservaService.verificarDisponibilidade(q.getId(), reserva.getDataCheckIn(), reserva.getDataCheckOut(), reserva.getId())) {
                                opcoesDeQuarto.add(q);
                            }
                        } catch (IllegalArgumentException e) {
                        }
                    }
                }
            }
            cmbCIQuartoDisponivel.setItems(FXCollections.observableArrayList(opcoesDeQuarto));
            cmbCIQuartoDisponivel.setValue(reserva.getQuarto());
            cmbCIQuartoDisponivel.setPromptText("Manter/Trocar Quarto");

            txtCIObservacoes.clear();

            paneDetalhesCheckIn.setVisible(true);
            paneDetalhesCheckIn.setManaged(true);
            
            boolean podeCheckIn = (reserva.getStatus() == StatusReserva.CONFIRMADA || reserva.getStatus() == StatusReserva.PENDENTE) &&
                                  reserva.getDataCheckIn().equals(dateFiltroReferencia.getValue());
            btnConfirmarCheckIn.setDisable(!podeCheckIn);
        } else {
            limparDetalhesCheckIn();
        }
    }

    private void limparDetalhesCheckIn() {
        paneDetalhesCheckIn.setVisible(false);
        paneDetalhesCheckIn.setManaged(false);
        btnConfirmarCheckIn.setDisable(true);
        lblCINomeHospede.setText("N/D"); lblCICPF.setText("N/D"); lblCITelefone.setText("N/D"); lblCIEmail.setText("N/D");
        lblCINumQuarto.setText("N/D"); lblCITipoQuarto.setText("N/D"); lblCIDatasEstadia.setText("N/D"); 
        lblCIValorTotal.setText("N/D"); lblCIStatusPagamento.setText("N/D");
        cmbCIQuartoDisponivel.getItems().clear();
        cmbCIQuartoDisponivel.setDisable(true);
        cmbCIQuartoDisponivel.setPromptText("Trocar quarto (opcional)");
        txtCIObservacoes.clear();
    }

    @FXML
    private void handleConfirmarCheckIn() {
        ReservaViewModel selecionadaVM = tblChegadas.getSelectionModel().getSelectedItem();
        if (selecionadaVM == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para check-in.");
            return;
        }

        Reserva reserva = reservaService.findReservaPorId(selecionadaVM.idProperty().get());
        if (reserva == null || reserva.getQuarto() == null ) {
            mostrarAlerta("Erro", "Reserva ou quarto original da reserva não encontrado.");
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

        if (quartoFoiTrocado) {
            if (!reservaService.verificarDisponibilidade(quartoAtualizadoNoSistema.getId(), reserva.getDataCheckIn(), reserva.getDataCheckOut(), 0)) {
                mostrarAlerta("Conflito de Reserva", "O novo quarto selecionado (" + quartoAtualizadoNoSistema.getNumeroQuarto() + ") não está disponível para todo o período da reserva.");
                return;
            }
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

            String obs = txtCIObservacoes.getText();
            if(obs != null && !obs.trim().isEmpty()){
            }
            
            // É importante salvar a reserva se ela foi modificada (quarto trocado, status, observações, dataRealCheckIn)
            // boolean reservaModificada = quartoFoiTrocado || (reserva.getStatus() != StatusReserva.HOSPEDADO) /* ou outra condição */;
            // if(reservaModificada) {
            //     if (!reservaService.upReserva(reserva)) {
            //         mostrarAlerta("Aviso", "Status do quarto atualizado, mas falha ao salvar outras alterações na reserva.");
            //          // Considerar reverter status do quarto aqui (transacionalidade)
            //     }
            // }

            mostrarAlerta("Sucesso", "Check-in realizado para " + reserva.getHospede().getNome() + 
                          " no quarto " + quartoAtualizadoNoSistema.getNumeroQuarto() + ".");
            
            atualizarTodasAsListas();
            limparDetalhesCheckIn();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro no Check-in", "Ocorreu um erro: " + e.getMessage());
        }
    }
    

    private void mostrarDetalhesCheckOut(ReservaViewModel rvm) {
        limparDetalhesCheckOut();
        if (rvm == null) return;

        Reserva reserva = reservaService.findReservaPorId(rvm.idProperty().get());
        if (reserva != null && reserva.getHospede() != null && reserva.getQuarto() != null) {
            lblCONomeHospede.setText(reserva.getHospede().getNome());
            lblCONumQuarto.setText(String.valueOf(reserva.getQuarto().getNumeroQuarto()));
            String datas = Formatter.formatDate(reserva.getDataCheckIn()) + " até " + Formatter.formatDate(reserva.getDataCheckOut());
            lblCODatasEstadia.setText(datas);

            // TODO: Implementar cálculo real de conta (Valor Reserva + Consumos - Pagamentos)
            // BigDecimal valorReservaOriginal = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO;
            // BigDecimal totalConsumos = consumoService.calcularTotalConsumos(reserva.getId());
            // BigDecimal totalPago = pagamentoService.calcularTotalPago(reserva.getId());
            // BigDecimal saldoDevedor = valorReservaOriginal.add(totalConsumos).subtract(totalPago);
            
            // Simulação por agora:
            BigDecimal saldoDevedorSimulado = reserva.getValorTotal() != null ? reserva.getValorTotal() : BigDecimal.ZERO; // Assume que o valor total da reserva é o saldo inicial

            lblCOTotalConta.setText(Formatter.formatCurrency(saldoDevedorSimulado)); // Simplificado
            lblCOValorJaPago.setText(Formatter.formatCurrency(BigDecimal.ZERO)); // Simulado
            lblCOSaldoDevedor.setText(Formatter.formatCurrency(saldoDevedorSimulado)); // Simulado

            // TODO: Popular tblCOContaConsumo com itens (diárias, produtos, serviços)
            // ObservableList<String> itensConta = FXCollections.observableArrayList();
            // itensConta.add("Diárias: " + Formatter.formatCurrency(reserva.getValorTotal()));
            // consumos.forEach(c -> itensConta.add(c.getProduto().getNome() + ": " + Formatter.formatCurrency(c.getValorTotal())));
            // tblCOContaConsumo.setItems(itensConta);


            paneDetalhesCheckOut.setVisible(true);
            paneDetalhesCheckOut.setManaged(true);
            
            boolean podeCheckOut = reserva.getStatus() == StatusReserva.CONFIRMADA;
            btnCOConfirmarCheckOut.setDisable(!podeCheckOut);
            btnCORegistrarPagamento.setDisable(!(podeCheckOut && saldoDevedorSimulado.compareTo(BigDecimal.ZERO) > 0));
            
            if(reserva.getStatus() == StatusReserva.CONCLUIDA || reserva.getStatus() == StatusReserva.CANCELADA){
                 btnCOConfirmarCheckOut.setDisable(true);
                 btnCORegistrarPagamento.setDisable(true);
            }
        }
    }
    
    private void limparDetalhesCheckOut() {
        paneDetalhesCheckOut.setVisible(false);
        paneDetalhesCheckOut.setManaged(false);
        btnCOConfirmarCheckOut.setDisable(true);
        btnCORegistrarPagamento.setDisable(true);
        lblCONomeHospede.setText("N/D"); lblCONumQuarto.setText("N/D"); lblCODatasEstadia.setText("N/D");
        tblCOContaConsumo.getItems().clear();
        lblCOTotalConta.setText("R$ 0,00"); lblCOValorJaPago.setText("R$ 0,00"); lblCOSaldoDevedor.setText("R$ 0,00");
        cmbCOMetodoPagamento.getSelectionModel().clearSelection();
        txtCOValorPago.clear();
    }

    @FXML
    private void handleConfirmarCheckOut() {
        ReservaViewModel selecionadaVM = tblHospedesSaidas.getSelectionModel().getSelectedItem();
        if (selecionadaVM == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada para check-out.");
            return;
        }

        Reserva reserva = reservaService.findReservaPorId(selecionadaVM.idProperty().get());
        if (reserva == null || reserva.getQuarto() == null) {
            mostrarAlerta("Erro", "Reserva ou quarto associado não encontrado.");
            return;
        }
        if (!lblCOSaldoDevedor.getText().equals(Formatter.formatCurrency(BigDecimal.ZERO)) &&
            !lblCOSaldoDevedor.getText().equalsIgnoreCase("Pago")) {
            
            Alert confirmacaoPagamento = new Alert(Alert.AlertType.CONFIRMATION, 
                "O saldo devedor é " + lblCOSaldoDevedor.getText() + ". Deseja prosseguir com o check-out mesmo assim?",
                ButtonType.YES, ButtonType.NO);
            confirmacaoPagamento.setTitle("Saldo Pendente");
            confirmacaoPagamento.setHeaderText("Check-out com Pendência Financeira");
            Optional<ButtonType> resultado = confirmacaoPagamento.showAndWait();
            if (resultado.isEmpty() || resultado.get() == ButtonType.NO) {
                return; 
            }
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
                mostrarAlerta("Aviso", "Status do quarto atualizado, mas falha ao concluir a reserva.");
                
            }

            mostrarAlerta("Sucesso", "Check-out realizado para " + reserva.getHospede().getNome() + 
                          " do quarto " + quarto.getNumeroQuarto() + ".");
            
            atualizarTodasAsListas();
            limparDetalhesCheckOut();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro no Check-out", "Ocorreu um erro: " + e.getMessage());
        }
    }
    
    @FXML
    private void handleRegistrarPagamentoFinal() {
        ReservaViewModel selecionadaVM = tblHospedesSaidas.getSelectionModel().getSelectedItem();
        if (selecionadaVM == null) {
            mostrarAlerta("Ação Inválida", "Nenhuma reserva selecionada.");
            return;
        }
        MetodoPagamento metodo = cmbCOMetodoPagamento.getValue();
        String valorPagoStr = txtCOValorPago.getText();

        if (metodo == null) {
             mostrarAlerta("Dados Incompletos", "Por favor, selecione o método de pagamento.");
             return;
        }
        if (valorPagoStr == null || valorPagoStr.trim().isEmpty()) {
            mostrarAlerta("Dados Incompletos", "Por favor, informe o valor pago.");
            return;
        }

        BigDecimal valorPago;
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            String pattern = "#,##0.00";
            DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
            decimalFormat.setParseBigDecimal(true);
            valorPago = (BigDecimal) decimalFormat.parse(valorPagoStr.trim());

            if (valorPago.compareTo(BigDecimal.ZERO) <= 0) {
                mostrarAlerta("Valor Inválido", "O valor do pagamento deve ser positivo.");
                return;
            }
        } catch (java.text.ParseException e) {
             try {
                 valorPago = new BigDecimal(valorPagoStr.trim().replace(",", "."));
                 if (valorPago.compareTo(BigDecimal.ZERO) <= 0) {
                    mostrarAlerta("Valor Inválido", "O valor do pagamento deve ser positivo.");
                    return;
                 }
             } catch (NumberFormatException nfe) {
                mostrarAlerta("Valor Inválido", "Por favor, insira um valor numérico válido para o pagamento (ex: 150,50).");
                return;
             }
        }
            
        Reserva reserva = reservaService.findReservaPorId(selecionadaVM.idProperty().get());
        // TODO: Integrar com PagamentoService
        // Pagamento novoPagamento = new Pagamento();
        // novoPagamento.setReservaId(reserva.getId());
        // novoPagamento.setValor(valorPago);
        // novoPagamento.setMetodo(metodo);
        // novoPagamento.setDataPagamento(LocalDate.now());
        // novoPagamento.setStatus(StatusPagamento.PAGO); 
        // boolean sucesso = pagamentoService.addPagamento(novoPagamento);

        // if (sucesso) {
        //    mostrarAlerta("Sucesso", "Pagamento de " + Formatter.formatCurrency(valorPago) + " registrado.");
        //    mostrarDetalhesCheckOut(selecionadaVM); // Atualiza o painel de detalhes e o saldo
        //    txtCOValorPago.clear();
        //    cmbCOMetodoPagamento.getSelectionModel().clearSelection();
        // } else {
        //    mostrarAlerta("Erro", "Não foi possível registrar o pagamento.");
        // }
        mostrarAlerta("Informação", "Lógica de registrar pagamento (PagamentoService) não implementada.\n" +
                                  "Valor: " + Formatter.formatCurrency(valorPago) + ", Método: " + metodo);
        // Simular atualização para UI
        mostrarDetalhesCheckOut(selecionadaVM); // Para recalcular e mostrar o saldo (ainda simulado)
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