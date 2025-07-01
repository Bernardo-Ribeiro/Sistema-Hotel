package com.hotel.gerenciador.controllers;

import com.hotel.gerenciador.models.Manutencao;
import com.hotel.gerenciador.models.Quarto;
import com.hotel.gerenciador.models.Funcionario;
import com.hotel.gerenciador.services.ManutencaoService;
import com.hotel.gerenciador.services.QuartoService;
import com.hotel.gerenciador.services.FuncionarioService;
import com.hotel.gerenciador.utils.Formatter;
import com.hotel.gerenciador.utils.StatusManutencao;
import com.hotel.gerenciador.utils.StatusQuarto;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GerenciamentoManutencaoController {

    @FXML private ComboBox<StatusManutencao> cmbFiltroStatus;
    @FXML private TextField txtFiltroQuarto;
    @FXML private Button btnAplicarFiltros;
    @FXML private Button btnLimparFiltros;

    @FXML private TableView<Manutencao> tblManutencoes;
    @FXML private TableColumn<Manutencao, Integer> colIdManutencao;
    @FXML private TableColumn<Manutencao, String> colIdQuarto;
    @FXML private TableColumn<Manutencao, String> colDescricao;
    @FXML private TableColumn<Manutencao, String> colDataInicio;
    @FXML private TableColumn<Manutencao, String> colDataFim;
    @FXML private TableColumn<Manutencao, String> colStatus;
    @FXML private TableColumn<Manutencao, String> colIdFuncionario;

    @FXML private Button btnNovoChamado;
    @FXML private Button btnMarcarEmAndamento;
    @FXML private Button btnConcluirChamado;
    @FXML private Button btnExcluirChamado;

    private ManutencaoService manutencaoService;
    private QuartoService quartoService;
    private FuncionarioService funcionarioService;
    private ObservableList<Manutencao> listaManutencoesObservavel;

    public GerenciamentoManutencaoController() {
        this.manutencaoService = new ManutencaoService();
        this.quartoService = new QuartoService();
        this.funcionarioService = new FuncionarioService();
        this.listaManutencoesObservavel = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        configurarTabelaManutencoes();
        configurarFiltros();
        carregarManutencoesNaTabela();
        configurarBotoesAcao(null);

        tblManutencoes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> configurarBotoesAcao(newSelection)
        );
    }

    private void configurarTabelaManutencoes() {
        colIdManutencao.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIdQuarto.setCellValueFactory(cellData -> {
            Quarto quarto = quartoService.findQuartoPorId(cellData.getValue().getIdQuarto());
            return new SimpleStringProperty(quarto != null ? String.valueOf(quarto.getNumeroQuarto()) : "N/A");
        });
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));
        colDataInicio.setCellValueFactory(cellData -> new SimpleStringProperty(Formatter.formatDate(cellData.getValue().getDataInicio())));
        colDataFim.setCellValueFactory(cellData -> new SimpleStringProperty(Formatter.formatDate(cellData.getValue().getDataFim())));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus().toString()));
        colIdFuncionario.setCellValueFactory(cellData -> {
            Funcionario funcionario = funcionarioService.findFuncionarioPorId(cellData.getValue().getIdFuncionario());
            return new SimpleStringProperty(funcionario != null ? funcionario.getNome() : "N/A");
        });

        tblManutencoes.setItems(listaManutencoesObservavel);
    }

    private void configurarFiltros() {
        ObservableList<StatusManutencao> statusOptions = FXCollections.observableArrayList(StatusManutencao.values());
        statusOptions.add(0, null);
        cmbFiltroStatus.setItems(statusOptions);
        cmbFiltroStatus.setConverter(new javafx.util.StringConverter<StatusManutencao>() {
            @Override
            public String toString(StatusManutencao status) {
                return status == null ? "Todos Status" : status.toString();
            }
            @Override
            public StatusManutencao fromString(String string) {
                if (string == null || string.equals("Todos Status")) return null;
                return StatusManutencao.valueOf(string);
            }
        });
        cmbFiltroStatus.getSelectionModel().selectFirst();
    }
    
    private void configurarBotoesAcao(Manutencao selecionada) {
        boolean nenhumaSelecionada = (selecionada == null);
        btnMarcarEmAndamento.setDisable(nenhumaSelecionada || selecionada.getStatus() != StatusManutencao.PENDENTE);
        btnConcluirChamado.setDisable(nenhumaSelecionada || selecionada.getStatus() == StatusManutencao.CONCLUIDA);
        btnExcluirChamado.setDisable(nenhumaSelecionada);
    }

    @FXML
    private void handleAplicarFiltros() {
        carregarManutencoesNaTabela();
    }

    @FXML
    private void handleLimparFiltros() {
        cmbFiltroStatus.getSelectionModel().selectFirst();
        txtFiltroQuarto.clear();
        carregarManutencoesNaTabela();
    }

    private void carregarManutencoesNaTabela() {
        List<Manutencao> manutencoesDoBanco;
        StatusManutencao statusFiltro = cmbFiltroStatus.getValue();
        String quartoFiltroStr = txtFiltroQuarto.getText().trim();
        Integer quartoIdFiltro = null;

        if (!quartoFiltroStr.isEmpty()) {
            try {
                int numeroQuartoInput = Integer.parseInt(quartoFiltroStr);
                Quarto quartoEncontrado = quartoService.findQuartoByNumero(numeroQuartoInput);
                if (quartoEncontrado != null) {
                    quartoIdFiltro = quartoEncontrado.getId();
                } else {
                    mostrarAlerta("Filtro Inválido", "Nenhum quarto encontrado com o número: " + numeroQuartoInput, Alert.AlertType.WARNING);
                    listaManutencoesObservavel.clear();
                    tblManutencoes.setPlaceholder(new Label("Quarto não encontrado para filtro."));
                    return;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Erro de Filtro", "Número do quarto inválido para filtro. Insira apenas números.", Alert.AlertType.WARNING);
                listaManutencoesObservavel.clear();
                tblManutencoes.setPlaceholder(new Label("Número do quarto inválido para filtro."));
                return;
            }
        }
        
        if (statusFiltro != null) {
            manutencoesDoBanco = manutencaoService.listManutencaoPorStatus(statusFiltro);
        } else {
            manutencoesDoBanco = manutencaoService.getAllManutencoes();
        }

        listaManutencoesObservavel.clear();
        if (manutencoesDoBanco != null) {
            final Integer finalQuartoIdFiltro = quartoIdFiltro;
            List<Manutencao> manutencoesFiltradas = manutencoesDoBanco.stream()
                .filter(m -> finalQuartoIdFiltro == null || m.getIdQuarto().equals(finalQuartoIdFiltro))
                .collect(Collectors.toList());
            listaManutencoesObservavel.addAll(manutencoesFiltradas);
        }
        
        if (listaManutencoesObservavel.isEmpty()) {
            tblManutencoes.setPlaceholder(new Label("Nenhum chamado de manutenção encontrado com os filtros aplicados."));
        }
    }

    @FXML
    private void handleNovoChamado() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NovoChamadoManutencao.fxml"));
            Parent root = loader.load();

            NovoChamadoManutencaoController controller = loader.getController();
            controller.setOnChamadoSalvoCallback(this::carregarManutencoesNaTabela);

            Stage stage = new Stage();
            stage.setTitle("Registrar Novo Chamado de Manutenção");
            stage.initModality(Modality.APPLICATION_MODAL);
             if (tblManutencoes.getScene() != null && tblManutencoes.getScene().getWindow() != null) {
                 stage.initOwner(tblManutencoes.getScene().getWindow());
            }
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Erro ao Abrir Tela", "Não foi possível abrir a tela de registro de novo chamado.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleMarcarEmAndamento() {
        Manutencao selecionada = tblManutencoes.getSelectionModel().getSelectedItem();
        if (selecionada == null || selecionada.getStatus() != StatusManutencao.PENDENTE) {
            mostrarAlerta("Ação Inválida", "Selecione um chamado PENDENTE para marcar como EM ANDAMENTO.", Alert.AlertType.WARNING);
            return;
        }

        if (manutencaoService.upStatus(selecionada.getId(), StatusManutencao.EM_ANDAMENTO)) {
            mostrarAlerta("Sucesso", "Chamado ID " + selecionada.getId() + " marcado como EM ANDAMENTO.", Alert.AlertType.INFORMATION);
            carregarManutencoesNaTabela();
        } else {
            mostrarAlerta("Erro", "Falha ao atualizar o status do chamado.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleConcluirChamado() {
        Manutencao selecionada = tblManutencoes.getSelectionModel().getSelectedItem();
        if (selecionada == null || selecionada.getStatus() == StatusManutencao.CONCLUIDA) {
            mostrarAlerta("Ação Inválida", "Selecione um chamado PENDENTE ou EM ANDAMENTO para concluir.", Alert.AlertType.WARNING);
            return;
        }
        
        TextInputDialog dialogDataFim = new TextInputDialog(LocalDate.now().toString());
        dialogDataFim.setTitle("Concluir Manutenção");
        dialogDataFim.setHeaderText("Concluir chamado ID: " + selecionada.getId() + " para o Quarto Nº: " + selecionada.getIdQuarto());
        dialogDataFim.setContentText("Data de Conclusão (AAAA-MM-DD):");
        Optional<String> dataResult = dialogDataFim.showAndWait();

        if (dataResult.isPresent() && !dataResult.get().trim().isEmpty()) {
            try {
                LocalDate dataFimConclusao = LocalDate.parse(dataResult.get().trim());
                if (dataFimConclusao.isBefore(selecionada.getDataInicio())) {
                    mostrarAlerta("Data Inválida", "A data de conclusão não pode ser anterior à data de início.", Alert.AlertType.ERROR);
                    return;
                }

                selecionada.setDataFim(dataFimConclusao);
                selecionada.setStatus(StatusManutencao.CONCLUIDA);

                if (manutencaoService.updateManutencao(selecionada)) { 
                    Quarto quarto = quartoService.findQuartoPorId(selecionada.getIdQuarto());
                    if (quarto != null) {
                        boolean outrasManutencoesAtivas = false;
                        List<Manutencao> todasManutencoes = manutencaoService.getAllManutencoes();
                        if (todasManutencoes != null) {
                            outrasManutencoesAtivas = todasManutencoes.stream()
                                .anyMatch(m -> m.getIdQuarto().equals(selecionada.getIdQuarto()) &&
                                            m.getId() != selecionada.getId() &&
                                            (m.getStatus() == StatusManutencao.PENDENTE || m.getStatus() == StatusManutencao.EM_ANDAMENTO));
                        }

                        if (!outrasManutencoesAtivas) {
                            quarto.setStatus(StatusQuarto.DISPONIVEL);
                            boolean atualizado = quartoService.upQuarto(quarto);
                            if (atualizado) {
                                mostrarAlerta("Status do Quarto Atualizado", "O quarto Nº " + quarto.getNumeroQuarto() + " agora está DISPONÍVEL.", Alert.AlertType.INFORMATION);
                            } else {
                                mostrarAlerta("Erro ao Atualizar Quarto", "Não foi possível atualizar o status do quarto para DISPONÍVEL.", Alert.AlertType.ERROR);
                            }
                        } else {
                            mostrarAlerta("Aviso", "Quarto Nº " + quarto.getNumeroQuarto() + " ainda possui outras manutenções ativas. Status do quarto não alterado para DISPONÍVEL.", Alert.AlertType.INFORMATION);
                        }
                    }
                    mostrarAlerta("Sucesso", "Chamado ID " + selecionada.getId() + " concluído.", Alert.AlertType.INFORMATION);
                    carregarManutencoesNaTabela();
                } else {
                    mostrarAlerta("Erro", "Falha ao concluir o chamado.", Alert.AlertType.ERROR);
                }
            } catch (java.time.format.DateTimeParseException e) {
                mostrarAlerta("Formato de Data Inválido", "Por favor, insira a data no formato AAAA-MM-DD.", Alert.AlertType.ERROR);
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro Inesperado", "Ocorreu um erro ao concluir o chamado: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    
    @FXML
    private void handleExcluirChamado() {
        Manutencao selecionada = tblManutencoes.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlerta("Ação Inválida", "Nenhum chamado selecionado para exclusão.", Alert.AlertType.WARNING);
            return;
        }

        if (selecionada.getStatus() == StatusManutencao.EM_ANDAMENTO) {
            mostrarAlerta("Ação Inválida", "Não é possível excluir um chamado EM ANDAMENTO. Conclua-o ou cancele-o primeiro (se houver essa opção).", Alert.AlertType.WARNING);
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Tem certeza que deseja excluir o chamado de manutenção ID " + selecionada.getId() + " para o quarto " + selecionada.getIdQuarto() + "?\nEsta ação não pode ser desfeita.",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Exclusão de Chamado");
        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            boolean sucesso = false;
            try {
                sucesso = manutencaoService.deleteManutencao(selecionada.getId()); 
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro de Exclusão", "Erro ao tentar excluir: " + e.getMessage(), Alert.AlertType.ERROR);
                return;
            }

            if (sucesso) {
                mostrarAlerta("Sucesso", "Chamado de manutenção excluído.", Alert.AlertType.INFORMATION);
                // Atualizar status do quarto se não houver outras manutenções ativas
                Quarto quarto = quartoService.findQuartoPorId(selecionada.getIdQuarto());
                if (quarto != null) {
                    boolean outrasManutencoesAtivas = false;
                    List<Manutencao> todasManutencoes = manutencaoService.getAllManutencoes();
                    if (todasManutencoes != null) {
                        outrasManutencoesAtivas = todasManutencoes.stream()
                            .anyMatch(m -> m.getIdQuarto().equals(selecionada.getIdQuarto()) &&
                                        (m.getStatus() == StatusManutencao.PENDENTE || m.getStatus() == StatusManutencao.EM_ANDAMENTO));
                    }
                    if (!outrasManutencoesAtivas) {
                        quarto.setStatus(StatusQuarto.DISPONIVEL);
                        boolean atualizado = quartoService.upQuarto(quarto);
                        if (atualizado) {
                            mostrarAlerta("Status do Quarto Atualizado", "O quarto Nº " + quarto.getNumeroQuarto() + " agora está DISPONÍVEL.", Alert.AlertType.INFORMATION);
                        } else {
                            mostrarAlerta("Erro ao Atualizar Quarto", "Não foi possível atualizar o status do quarto para DISPONÍVEL.", Alert.AlertType.ERROR);
                        }
                    }
                }
                carregarManutencoesNaTabela();
            } else {
                mostrarAlerta("Erro", "Falha ao excluir o chamado de manutenção.", Alert.AlertType.ERROR);
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}