package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Hospede;
import com.hotel.gerenciador.model.Quarto;
import com.hotel.gerenciador.model.Reserva;
import com.hotel.gerenciador.service.HospedeService;
import com.hotel.gerenciador.service.QuartoService;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.StatusReserva;
import com.hotel.gerenciador.util.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class EditarReservaController {

    @FXML
    private VBox rootEditarReservaPane;

    @FXML
    private ComboBox<Hospede> cmbHospede;

    @FXML
    private ComboBox<Quarto> cmbQuarto;

    @FXML
    private DatePicker datePickerCheckIn;

    @FXML
    private DatePicker datePickerCheckOut;

    @FXML
    private ComboBox<StatusReserva> cmbStatusReserva;

    @FXML
    private Label lblValorTotalCalculado;

    // @FXML private Button btnSalvarAlteracoes; // Se o fx:id do botão salvar for este
    // @FXML private Button btnCancelarEdicao;  // Se o fx:id do botão cancelar for este


    private HospedeService hospedeService;
    private QuartoService quartoService;
    private ReservaService reservaService;

    private Reserva reservaAtual;
    private Runnable onReservaSalvaCallback;
    private ObservableList<Quarto> todosOsQuartosCache;

    public EditarReservaController() {
        this.hospedeService = new HospedeService();
        this.quartoService = new QuartoService();
        this.reservaService = new ReservaService();
    }

    @FXML
    private void initialize() {
        this.todosOsQuartosCache = FXCollections.observableArrayList(quartoService.findAll());
        
        carregarHospedes();
        carregarStatusReserva();

        datePickerCheckIn.valueProperty().addListener((obs, oldDate, newDate) -> handleDateChange());
        datePickerCheckOut.valueProperty().addListener((obs, oldDate, newDate) -> handleDateChange());
        cmbQuarto.valueProperty().addListener((obs, oldQuarto, newQuarto) -> recalcularValorTotal());

        cmbHospede.setConverter(new StringConverter<Hospede>() {
            @Override
            public String toString(Hospede hospede) {
                return hospede == null ? "Selecione o Hóspede" : hospede.getNome() + " (CPF: " + Formatter.formatCpf(hospede.getCpf()) + ")";
            }
            @Override
            public Hospede fromString(String string) { return null; }
        });
        cmbHospede.setPromptText("Selecione o Hóspede");

        cmbQuarto.setConverter(new StringConverter<Quarto>() {
            @Override
            public String toString(Quarto quarto) {
                return quarto == null ? cmbQuarto.getPromptText() : "Nº: " + quarto.getNumeroQuarto() + " (" + quarto.getTipo() + ") - " + Formatter.formatCurrency(quarto.getPrecoDiaria());
            }
            @Override
            public Quarto fromString(String string) { return null; }
        });
        cmbQuarto.setPromptText("Selecione as datas");
        cmbQuarto.setDisable(true);
    }

    public void setOnReservaSalva(Runnable callback) {
        this.onReservaSalvaCallback = callback;
    }

    public void carregarDadosReserva(int reservaId) {
        this.reservaAtual = reservaService.findReservaPorId(reservaId);

        if (this.reservaAtual != null) {
            cmbHospede.setValue(this.reservaAtual.getHospede());
            datePickerCheckIn.setValue(this.reservaAtual.getDataCheckIn());
            datePickerCheckOut.setValue(this.reservaAtual.getDataCheckOut());
            cmbStatusReserva.setValue(this.reservaAtual.getStatus());

            handleDateChange();

            if (cmbQuarto.getItems().contains(this.reservaAtual.getQuarto())) {
                 cmbQuarto.setValue(this.reservaAtual.getQuarto());
            }
            
            recalcularValorTotal();

            if (reservaAtual.getStatus() == StatusReserva.CONCLUIDA || reservaAtual.getStatus() == StatusReserva.CANCELADA) {
                cmbHospede.setDisable(true);
                cmbQuarto.setDisable(true);
                datePickerCheckIn.setDisable(true);
                datePickerCheckOut.setDisable(true);
                cmbStatusReserva.setDisable(true); 
                // btnSalvarAlteracoes.setDisable(true); // Se o botão de salvar tiver fx:id
            }

        } else {
            mostrarAlerta("Erro", "Reserva não encontrada para edição.");
            fecharJanela();
        }
    }

    private void carregarHospedes() {
        List<Hospede> hospedes = hospedeService.getAllHospedes();
        cmbHospede.setItems(hospedes != null ? FXCollections.observableArrayList(hospedes) : FXCollections.emptyObservableList());
    }

    private void carregarStatusReserva() {
        cmbStatusReserva.setItems(FXCollections.observableArrayList(StatusReserva.values()));
    }
    
    private void handleDateChange() {
        LocalDate checkIn = datePickerCheckIn.getValue();
        LocalDate checkOut = datePickerCheckOut.getValue();
        Quarto quartoSelecionadoOriginalmente = (reservaAtual != null) ? reservaAtual.getQuarto() : null;
        Quarto quartoSelecionadoNaCombo = cmbQuarto.getValue();


        if (checkIn != null && checkOut != null && checkOut.isAfter(checkIn)) {
            cmbQuarto.setDisable(false);
            ObservableList<Quarto> quartosDisponiveis = FXCollections.observableArrayList();
            if (this.todosOsQuartosCache != null) {
                for (Quarto quarto : this.todosOsQuartosCache) {
                    boolean disponivel = false;
                    try {
                        disponivel = reservaService.verificarDisponibilidade(quarto.getId(), checkIn, checkOut, (reservaAtual != null ? reservaAtual.getId() : 0) );
                    } catch (IllegalArgumentException e) {
                        System.err.println("Filtro de quarto (edição): Datas inválidas (" + e.getMessage() + "). Quarto " + quarto.getNumeroQuarto() + " indisponível.");
                    }
                    if (disponivel) {
                        quartosDisponiveis.add(quarto);
                    }
                }
            }

            cmbQuarto.setItems(quartosDisponiveis);
            cmbQuarto.setPromptText(quartosDisponiveis.isEmpty() ? "Nenhum quarto disponível" : "Selecione o Quarto");

            if (quartoSelecionadoNaCombo != null && quartosDisponiveis.contains(quartoSelecionadoNaCombo)) {
                cmbQuarto.setValue(quartoSelecionadoNaCombo);
            } 
            else if (quartoSelecionadoOriginalmente != null && quartosDisponiveis.contains(quartoSelecionadoOriginalmente)) {
                cmbQuarto.setValue(quartoSelecionadoOriginalmente);
            }
            else if (!quartosDisponiveis.isEmpty()) {
                cmbQuarto.getSelectionModel().selectFirst();
            } 
            else {
                cmbQuarto.getSelectionModel().clearSelection();
            }

        } else {
            cmbQuarto.setDisable(true);
            cmbQuarto.getItems().clear();
            cmbQuarto.getSelectionModel().clearSelection();
            cmbQuarto.setPromptText("Selecione datas válidas");
        }
        recalcularValorTotal();
    }


    private void recalcularValorTotal() {
        Quarto quartoSelecionado = cmbQuarto.getValue();
        LocalDate dataCheckIn = datePickerCheckIn.getValue();
        LocalDate dataCheckOut = datePickerCheckOut.getValue();

        if (quartoSelecionado != null && dataCheckIn != null && dataCheckOut != null && dataCheckOut.isAfter(dataCheckIn)) {
            long dias = ChronoUnit.DAYS.between(dataCheckIn, dataCheckOut);
            if (dias > 0 && quartoSelecionado.getPrecoDiaria() != null) {
                BigDecimal valorTotal = quartoSelecionado.getPrecoDiaria().multiply(new BigDecimal(dias));
                lblValorTotalCalculado.setText(Formatter.formatCurrency(valorTotal));
            } else {
                lblValorTotalCalculado.setText(Formatter.formatCurrency(BigDecimal.ZERO));
            }
        } else {
            lblValorTotalCalculado.setText(Formatter.formatCurrency(BigDecimal.ZERO));
        }
    }

    @FXML
    private void handleSalvarAlteracoes() {
        if (reservaAtual == null) {
            mostrarAlerta("Erro", "Nenhuma reserva carregada para edição.");
            return;
        }
        
        if (reservaAtual.getStatus() == StatusReserva.CONCLUIDA || reservaAtual.getStatus() == StatusReserva.CANCELADA) {
            mostrarAlerta("Aviso", "Reservas com status " + reservaAtual.getStatus() + " não podem ser editadas.");
            return;
        }

        Hospede hospedeSelecionado = cmbHospede.getValue();
        Quarto quartoSelecionado = cmbQuarto.getValue();
        LocalDate checkIn = datePickerCheckIn.getValue();
        LocalDate checkOut = datePickerCheckOut.getValue();
        StatusReserva statusSelecionado = cmbStatusReserva.getValue();

        String msgErroValidacao = "";
        if (hospedeSelecionado == null) msgErroValidacao += "Selecione um hóspede.\n";
        if (quartoSelecionado == null) msgErroValidacao += "Selecione um quarto.\n";
        if (checkIn == null) msgErroValidacao += "Selecione uma data de check-in.\n";
        if (checkOut == null) msgErroValidacao += "Selecione uma data de check-out.\n";
        if (statusSelecionado == null) msgErroValidacao += "Selecione um status.\n";

        if (!msgErroValidacao.isEmpty()) {
            mostrarAlerta("Erro de Validação", msgErroValidacao.trim());
            return;
        }

        try {
            Validator.validateDateRange(checkIn, checkOut);
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação de Datas", e.getMessage());
            return;
        }

        boolean verificarDisp = true;
        if (reservaAtual.getQuarto() != null && reservaAtual.getQuarto().getId() == quartoSelecionado.getId() &&
            reservaAtual.getDataCheckIn().equals(checkIn) &&
            reservaAtual.getDataCheckOut().equals(checkOut)) {
            verificarDisp = false;
        }

        if (verificarDisp && !reservaService.verificarDisponibilidade(quartoSelecionado.getId(), checkIn, checkOut, reservaAtual.getId())) {
            mostrarAlerta("Conflito de Reserva", "O quarto selecionado não está disponível para o novo período informado.");
            return;
        }


        reservaAtual.setHospede(hospedeSelecionado);
        reservaAtual.setQuarto(quartoSelecionado);
        reservaAtual.setDataCheckIn(checkIn);
        reservaAtual.setDataCheckOut(checkOut);
        reservaAtual.setStatus(statusSelecionado);

        try {
            boolean sucesso = reservaService.upReserva(reservaAtual);
            if (sucesso) {
                mostrarAlerta("Sucesso", "Reserva atualizada com sucesso!");
                if (onReservaSalvaCallback != null) {
                    onReservaSalvaCallback.run();
                }
                fecharJanela();
            } else {
                mostrarAlerta("Erro ao Salvar", "Não foi possível atualizar a reserva.");
            }
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação ao Salvar", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro inesperado ao salvar a reserva: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelarEdicao() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) rootEditarReservaPane.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert.AlertType tipoAlerta = Alert.AlertType.INFORMATION;
        if (titulo.toLowerCase().contains("erro") || titulo.toLowerCase().contains("conflito")) {
            tipoAlerta = Alert.AlertType.ERROR;
        } else if (titulo.toLowerCase().contains("aviso")) {
            tipoAlerta = Alert.AlertType.WARNING;
        }
        Alert alert = new Alert(tipoAlerta);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}