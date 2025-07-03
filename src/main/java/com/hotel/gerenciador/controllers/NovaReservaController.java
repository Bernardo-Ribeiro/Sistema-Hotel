package com.hotel.gerenciador.controllers;

import com.hotel.gerenciador.models.Hospede;
import com.hotel.gerenciador.models.Quarto;
import com.hotel.gerenciador.models.Reserva;
import com.hotel.gerenciador.services.HospedeService;
import com.hotel.gerenciador.services.QuartoService;
import com.hotel.gerenciador.services.ReservaService;
import com.hotel.gerenciador.utils.Formatter;
import com.hotel.gerenciador.utils.StatusReserva;
import com.hotel.gerenciador.utils.Validator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NovaReservaController {

    @FXML
    private VBox rootNovaReservaPane;
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

    private HospedeService hospedeService;
    private QuartoService quartoService;
    private ReservaService reservaService;

    private Runnable onReservaSalvaCallback;
    private ObservableList<Quarto> todosOsQuartosCache;

    public NovaReservaController() {
        this.hospedeService = new HospedeService();
        this.quartoService = new QuartoService();
        this.reservaService = new ReservaService();
    }

    @FXML
    private void initialize() {
        carregarHospedes();
        this.todosOsQuartosCache = FXCollections.observableArrayList(quartoService.findAll()); 
        carregarStatusReserva();

        datePickerCheckIn.valueProperty().addListener((obs, oldDate, newDate) -> handleDateChange());
        datePickerCheckOut.valueProperty().addListener((obs, oldDate, newDate) -> handleDateChange());
        cmbQuarto.valueProperty().addListener((obs, oldQuarto, newQuarto) -> recalcularValorTotal());

        cmbHospede.setConverter(new javafx.util.StringConverter<Hospede>() {
            @Override
            public String toString(Hospede hospede) {
                return hospede == null ? "Selecione o Hóspede" : hospede.getNome() + " (CPF: " + Formatter.formatCpf(hospede.getCpf()) + ")";
            }
            @Override
            public Hospede fromString(String string) { return null; }
        });
        cmbHospede.setPromptText("Selecione o Hóspede");

        cmbQuarto.setConverter(new javafx.util.StringConverter<Quarto>() {
            @Override
            public String toString(Quarto quarto) {
                return quarto == null ? cmbQuarto.getPromptText() : "Nº: " + quarto.getNumeroQuarto() + " (" + quarto.getTipo() + ") - " + Formatter.formatCurrency(quarto.getPrecoDiaria());
            }
            @Override
            public Quarto fromString(String string) { return null; }
        });

        cmbStatusReserva.getSelectionModel().select(StatusReserva.PENDENTE);
        recalcularValorTotal(); 
    }
    
    public void setOnReservaSalva(Runnable callback) {
        this.onReservaSalvaCallback = callback;
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
        Quarto quartoSelecionadoAnteriormente = cmbQuarto.getValue();

        if (checkIn != null && checkOut != null && checkOut.isAfter(checkIn)) {
            cmbQuarto.setDisable(false);
            
            ObservableList<Quarto> quartosDisponiveis = FXCollections.observableArrayList();
            if (this.todosOsQuartosCache != null) {
                for (Quarto quarto : this.todosOsQuartosCache) {
                    boolean disponivel = false;
                    try {
                        disponivel = reservaService.verificarDisponibilidade(quarto.getId(), checkIn, checkOut);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Filtro de quarto: Datas inválidas para nova reserva (" + e.getMessage() + "). Quarto " + quarto.getNumeroQuarto() + " considerado indisponível para este range.");
                    }
                    if (disponivel) {
                        quartosDisponiveis.add(quarto);
                    }
                }
            }

            cmbQuarto.setItems(quartosDisponiveis);
            if (quartosDisponiveis.isEmpty()) {
                cmbQuarto.setPromptText("Nenhum quarto disponível");
                cmbQuarto.getSelectionModel().clearSelection();
            } else {
                cmbQuarto.setPromptText("Selecione o Quarto");
                if (quartoSelecionadoAnteriormente != null && quartosDisponiveis.contains(quartoSelecionadoAnteriormente)) {
                    cmbQuarto.setValue(quartoSelecionadoAnteriormente);
                } else {
                    cmbQuarto.getSelectionModel().selectFirst();
                }
            }
        } else {
            cmbQuarto.setDisable(true);
            cmbQuarto.getItems().clear();
            cmbQuarto.getSelectionModel().clearSelection();
            cmbQuarto.setPromptText("Selecione as datas primeiro");
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
    private void handleSalvarReserva() {
        Hospede hospede = cmbHospede.getValue();
        Quarto quarto = cmbQuarto.getValue();
        LocalDate checkIn = datePickerCheckIn.getValue();
        LocalDate checkOut = datePickerCheckOut.getValue();
        StatusReserva status = cmbStatusReserva.getValue();

        String msgErroValidacao = "";
        if (hospede == null) msgErroValidacao += "Selecione um hóspede.\n";
        if (checkIn == null) msgErroValidacao += "Selecione uma data de check-in.\n";
        if (checkOut == null) msgErroValidacao += "Selecione uma data de check-out.\n";
        if (quarto == null) msgErroValidacao += "Selecione um quarto (verifique as datas para disponibilidade).\n";
        if (status == null) msgErroValidacao += "Selecione um status para a reserva.\n";
        
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

        if (!reservaService.verificarDisponibilidade(quarto.getId(), checkIn, checkOut)) {
            mostrarAlerta("Conflito de Reserva", "O quarto selecionado não está mais disponível para o período informado.\nPor favor, verifique as datas ou selecione outro quarto.");

            handleDateChange(); 
            return;
        }
        
        Reserva novaReserva = new Reserva();
        novaReserva.setHospede(hospede);
        novaReserva.setQuarto(quarto);
        novaReserva.setDataCheckIn(checkIn);
        novaReserva.setDataCheckOut(checkOut);
        novaReserva.setStatus(status);

        try {
            boolean sucesso = reservaService.addReserva(novaReserva);
            if (sucesso) {
                mostrarAlerta("Sucesso", "Reserva adicionada com sucesso! ID: " + novaReserva.getId());
                if (onReservaSalvaCallback != null) {
                    onReservaSalvaCallback.run();
                }
                fecharJanela();
            } else {
                mostrarAlerta("Erro ao Salvar", "Não foi possível adicionar a reserva.");
            }
        } catch (IllegalArgumentException e) { 
            mostrarAlerta("Erro de Validação ao Salvar", e.getMessage());
        } catch (Exception e) { 
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro inesperado ao salvar a reserva: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelarNovaReserva() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) rootNovaReservaPane.getScene().getWindow();
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