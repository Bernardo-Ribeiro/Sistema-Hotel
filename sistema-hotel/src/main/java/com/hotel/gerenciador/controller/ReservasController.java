package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Reserva; // Removidas Hospede e Quarto se não usadas diretamente aqui
import com.hotel.gerenciador.viewmodel.ReservaViewModel;
import com.hotel.gerenciador.service.ReservaService;
import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.StatusReserva;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class ReservasController {

    @FXML private TableView<ReservaViewModel> tabelaReservas;
    @FXML private TableColumn<ReservaViewModel, Integer> colId;
    @FXML private TableColumn<ReservaViewModel, String> colCliente;
    @FXML private TableColumn<ReservaViewModel, String> colQuarto;
    @FXML private TableColumn<ReservaViewModel, String> colDataEntrada;
    @FXML private TableColumn<ReservaViewModel, String> colDataSaida;
    @FXML private TableColumn<ReservaViewModel, String> colStatus;
    @FXML private TableColumn<ReservaViewModel, String> colValorTotal;

    @FXML private ComboBox<StatusReserva> cmbFiltroStatus;
    @FXML private DatePicker dateFiltroCheckInDe;
    @FXML private DatePicker dateFiltroCheckInAte;
    @FXML private TextField txtFiltroBusca;
    // @FXML private Button btnAplicarFiltros; // Se não for usar, pode remover
    // @FXML private Button btnLimparFiltros; // Se não for usar, pode remover

    private ReservaService reservaService = new ReservaService();

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

    @FXML
    private void initialize() {
        colId.setCellValueFactory(cell -> cell.getValue().idProperty().asObject());
        colCliente.setCellValueFactory(cell -> cell.getValue().clienteProperty());
        colQuarto.setCellValueFactory(cell -> cell.getValue().quartoProperty());
        colDataEntrada.setCellValueFactory(cell -> cell.getValue().dataEntradaProperty());
        colDataSaida.setCellValueFactory(cell -> cell.getValue().dataSaidaProperty());
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        colValorTotal.setCellValueFactory(cell -> cell.getValue().valorTotalProperty());

        ObservableList<StatusReserva> statusOptions = FXCollections.observableArrayList();
        statusOptions.add(null);
        statusOptions.addAll(StatusReserva.values());
        cmbFiltroStatus.setItems(statusOptions);
        
        cmbFiltroStatus.setConverter(new StringConverter<StatusReserva>() {
            @Override
            public String toString(StatusReserva status) {
                return status == null ? "Todos Status" : status.toString();
            }
            @Override
            public StatusReserva fromString(String string) {
                if (string == null || string.equals("Todos Status")) return null;
                try {
                    return StatusReserva.valueOf(string);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        });
        cmbFiltroStatus.getSelectionModel().select(null);
        cmbFiltroStatus.setOnAction(event -> aplicarFiltros());
        dateFiltroCheckInDe.setOnAction(event -> aplicarFiltros());
        dateFiltroCheckInAte.setOnAction(event -> aplicarFiltros());
        txtFiltroBusca.setOnAction(event -> aplicarFiltros());
        aplicarFiltros(); 
    }

    @FXML
    private void aplicarFiltros() {
        StatusReserva statusSelecionado = cmbFiltroStatus.getValue(); 
        LocalDate dataDe = dateFiltroCheckInDe.getValue();
        LocalDate dataAte = dateFiltroCheckInAte.getValue();
        String termoBusca = txtFiltroBusca.getText() != null ? txtFiltroBusca.getText().trim() : null;

        if (dataDe != null && dataAte != null && dataDe.isAfter(dataAte)) {
            mostrarAlerta("Erro de Filtro", "A data 'De' (Check-in) não pode ser posterior à data 'Até' (Check-in).");
            tabelaReservas.setItems(FXCollections.emptyObservableList());
            return;
        }

        ObservableList<ReservaViewModel> reservasVM = FXCollections.observableArrayList();
        
        List<Reserva> listaFiltrada = reservaService.buscarReservasComFiltros(statusSelecionado, dataDe, dataAte, termoBusca);

        if (listaFiltrada != null) {
            for (Reserva r : listaFiltrada) {
                ReservaViewModel vm = toViewModel(r);
                if (vm != null) {
                    reservasVM.add(vm);
                }
            }
        }
        tabelaReservas.setItems(reservasVM);

        if (reservasVM.isEmpty()) {
            tabelaReservas.setPlaceholder(new Label("Nenhuma reserva encontrada com os filtros aplicados."));
        } else {
            tabelaReservas.setPlaceholder(null);
        }
    }

    @FXML
    private void limparFiltros() {
        cmbFiltroStatus.setValue(null); 
        dateFiltroCheckInDe.setValue(null);
        dateFiltroCheckInAte.setValue(null);
        txtFiltroBusca.clear();
        aplicarFiltros(); 
    }

    @FXML
    private void novaReserva() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NovaReserva.fxml"));
            Parent root = loader.load();
            NovaReservaController novaReservaController = loader.getController();
            novaReservaController.setOnReservaSalva(() -> aplicarFiltros());

            Stage stage = new Stage();
            stage.setTitle("Adicionar Nova Reserva");
            stage.initModality(Modality.APPLICATION_MODAL);
            
            if (tabelaReservas.getScene() != null && tabelaReservas.getScene().getWindow() != null) {
                 stage.initOwner(tabelaReservas.getScene().getWindow());
            }

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir a tela de nova reserva: " + e.getMessage());
        } catch (NullPointerException e) {
            e.printStackTrace();
             mostrarAlerta("Erro de Configuração", "Verifique o caminho para NovaReserva.fxml ou se o controller está configurado corretamente.");
        }
    }

    @FXML
    private void editarReserva() {
        ReservaViewModel selecionada = tabelaReservas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Aviso", "Por favor, selecione uma reserva para editar.");
            return;
        }
        System.out.println("Editar reserva ID: " + selecionada.idProperty().get());
        // TODO: Implementar a abertura da janela de edição, similar à novaReserva()
        // Passar o ID da reserva para o controller de edição para carregar os dados.
        // Ex: EditarReservaController editController = loader.getController();
        //     editController.carregarReserva(selecionada.idProperty().get());
        //     editController.setOnReservaSalva(() -> aplicarFiltros());
        mostrarAlerta("Informação", "Funcionalidade de edição ainda não implementada.");
    }

    @FXML
    private void cancelarReserva() {
        ReservaViewModel selecionada = tabelaReservas.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Aviso", "Por favor, selecione uma reserva para cancelar.");
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, 
            "Você tem certeza que deseja cancelar a reserva ID: " + selecionada.idProperty().get() + "?", 
            ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Cancelamento");
        confirmacao.setHeaderText(null);

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                try {
                    Reserva reservaOriginal = reservaService.findReservaPorId(selecionada.idProperty().get());
                    if (reservaOriginal != null && 
                        reservaOriginal.getStatus() != StatusReserva.CANCELADA &&
                        reservaOriginal.getStatus() != StatusReserva.CONCLUIDA) {
                        
                        reservaOriginal.setStatus(StatusReserva.CANCELADA);
                        boolean sucesso = reservaService.upReserva(reservaOriginal);
                        if (sucesso) {
                            mostrarAlerta("Sucesso", "Reserva ID " + selecionada.idProperty().get() + " cancelada.");
                            aplicarFiltros();
                        } else {
                            mostrarAlerta("Erro", "Não foi possível cancelar a reserva.");
                        }
                    } else {
                         mostrarAlerta("Aviso", "Esta reserva não pode mais ser cancelada (status atual: " + 
                                       (reservaOriginal != null ? reservaOriginal.getStatus() : "desconhecido") + ").");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mostrarAlerta("Erro", "Ocorreu um erro ao cancelar a reserva: " + e.getMessage());
                }
            }
        });
    }
    
    private void mostrarAlerta(String titulo, String mensagem) {
        Alert.AlertType tipo = Alert.AlertType.INFORMATION;
        String tituloLower = titulo.toLowerCase();

        if (tituloLower.contains("erro") || tituloLower.contains("falha")) {
            tipo = Alert.AlertType.ERROR;
        } else if (tituloLower.contains("aviso") || tituloLower.contains("atenção")) {
            tipo = Alert.AlertType.WARNING;
        } else if (tituloLower.contains("sucesso")) {
             tipo = Alert.AlertType.INFORMATION;
        }
        
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}