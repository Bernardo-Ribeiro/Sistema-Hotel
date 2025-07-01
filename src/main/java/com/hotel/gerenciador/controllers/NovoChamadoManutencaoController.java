package com.hotel.gerenciador.controllers;

import com.hotel.gerenciador.models.Funcionario;
import com.hotel.gerenciador.models.Manutencao;
import com.hotel.gerenciador.models.Quarto;
import com.hotel.gerenciador.services.FuncionarioService;
import com.hotel.gerenciador.services.ManutencaoService;
import com.hotel.gerenciador.services.QuartoService;
import com.hotel.gerenciador.utils.StatusManutencao;
import com.hotel.gerenciador.utils.StatusQuarto;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NovoChamadoManutencaoController {

    @FXML private ComboBox<Quarto> cmbQuarto;
    @FXML private DatePicker dpDataInicio;
    @FXML private ComboBox<Funcionario> cmbFuncionario;
    @FXML private TextArea txtDescricao;
    @FXML private Button btnSalvarChamado;
    @FXML private Button btnCancelar;

    private ManutencaoService manutencaoService;
    private QuartoService quartoService;
    private FuncionarioService funcionarioService;
    
    private Runnable onChamadoSalvoCallback;

    public NovoChamadoManutencaoController() {
        this.manutencaoService = new ManutencaoService();
        this.quartoService = new QuartoService();
        this.funcionarioService = new FuncionarioService();
    }

    public void setOnChamadoSalvoCallback(Runnable callback) {
        this.onChamadoSalvoCallback = callback;
    }

    @FXML
    private void initialize() {
        carregarQuartos();
        carregarFuncionarios();
        dpDataInicio.setValue(LocalDate.now());

        cmbQuarto.setConverter(new StringConverter<Quarto>() {
            @Override
            public String toString(Quarto quarto) {
                return quarto == null ? "Selecione um Quarto" : "Nº " + quarto.getNumeroQuarto() + " (" + quarto.getTipo() + ") - Status: " + quarto.getStatus();
            }
            @Override
            public Quarto fromString(String string) { return null; }
        });

        cmbFuncionario.setConverter(new StringConverter<Funcionario>() {
            @Override
            public String toString(Funcionario funcionario) {
                return funcionario == null ? "Selecione (Opcional)" : funcionario.getNome() + " (ID: " + funcionario.getId() + ")";
            }
            @Override
            public Funcionario fromString(String string) { return null; }
        });
    }

    private void carregarQuartos() {
        // Idealmente, carregar apenas quartos que não estejam já em manutenção severa,
        // mas para simplificar, carregamos todos e o usuário decide.
        List<Quarto> quartos = quartoService.findAll();
        cmbQuarto.setItems(quartos != null ? FXCollections.observableArrayList(quartos) : FXCollections.emptyObservableList());
    }

    private void carregarFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.getAllFuncionarios();
        cmbFuncionario.setItems(funcionarios != null ? FXCollections.observableArrayList(funcionarios) : FXCollections.emptyObservableList());
    }

    @FXML
    private void handleSalvarChamado() {
        Quarto quartoSelecionado = cmbQuarto.getValue();
        LocalDate dataInicio = dpDataInicio.getValue();
        String descricao = txtDescricao.getText();
        Funcionario funcionarioSelecionado = cmbFuncionario.getValue();

        try {
            if (quartoSelecionado == null) {
                throw new IllegalArgumentException("Selecione um quarto para a manutenção.");
            }
            if (dataInicio == null) {
                throw new IllegalArgumentException("A data de início é obrigatória.");
            }
            if (descricao == null || descricao.trim().isEmpty()) {
                throw new IllegalArgumentException("A descrição do problema é obrigatória.");
            }
            if (quartoSelecionado.getStatus() == StatusQuarto.MANUTENCAO) {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, 
                "O quarto selecionado já está em manutenção. Deseja registrar um novo chamado mesmo assim?", 
                ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Quarto já em Manutenção");
                Optional<ButtonType> res = confirm.showAndWait();
                if (res.isEmpty() || res.get() == ButtonType.NO) {
                    return;
                }
            }


            Manutencao novoChamado = new Manutencao(
                quartoSelecionado.getId(),
                dataInicio,
                null,
                descricao.trim(),
                StatusManutencao.PENDENTE,
                (funcionarioSelecionado != null ? funcionarioSelecionado.getId() : null)
            );

            if (manutencaoService.addManutencao(novoChamado)) {
                // Atualiza o status do quarto para MANUTENCAO
                // É importante que o QuartoService.upQuarto funcione corretamente
                Quarto quartoParaAtualizar = quartoService.findQuartoPorId(quartoSelecionado.getId());
                if (quartoParaAtualizar != null) {
                    quartoParaAtualizar.setStatus(StatusQuarto.MANUTENCAO);
                    quartoService.upQuarto(quartoParaAtualizar);
                }

                mostrarAlerta("Sucesso", "Novo chamado de manutenção registrado com sucesso!", Alert.AlertType.INFORMATION);
                if (onChamadoSalvoCallback != null) {
                    onChamadoSalvoCallback.run();
                }
                fecharJanela();
            } else {
                mostrarAlerta("Erro", "Falha ao registrar o novo chamado de manutenção.", Alert.AlertType.ERROR);
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage(), Alert.AlertType.WARNING);
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro Inesperado", "Ocorreu um erro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}