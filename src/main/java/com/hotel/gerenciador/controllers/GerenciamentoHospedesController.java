package com.hotel.gerenciador.controllers;

import com.hotel.gerenciador.models.Hospede;
import com.hotel.gerenciador.services.HospedeService;
import com.hotel.gerenciador.utils.Formatter;
import com.hotel.gerenciador.utils.Validator;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class GerenciamentoHospedesController {

    @FXML private TextField txtNomeHospede;
    @FXML private TextField txtCpfHospede;
    @FXML private TextField txtTelefoneHospede;
    @FXML private TextField txtEmailHospede;
    @FXML private TextField txtLogradouroHospede;
    @FXML private TextField txtBairroHospede;
    @FXML private TextField txtLocalidadeUfHospede;
    @FXML private TextField txtCepHospede;

    @FXML private DatePicker dpDataNascimentoHospede;
    @FXML private Button btnSalvarHospede;
    @FXML private Button btnNovoHospede;
    @FXML private Button btnExcluirHospede;

    @FXML private TextField txtBuscaHospede;
    @FXML private Button btnBuscarHospedes;

    @FXML private TableView<Hospede> tblHospedes;
    @FXML private TableColumn<Hospede, Integer> colHospedeId;
    @FXML private TableColumn<Hospede, String> colNomeHospede;
    @FXML private TableColumn<Hospede, String> colCpfHospede;
    @FXML private TableColumn<Hospede, String> colTelefoneHospede;
    @FXML private TableColumn<Hospede, String> colEmailHospede;

    private HospedeService hospedeService;
    private ObservableList<Hospede> listaHospedesObservavel;
    private Hospede hospedeSelecionadoParaEdicao = null;

    private boolean isFormattingCpf = false;
    private boolean isFormattingTelefone = false;
    private boolean isFormattingCep = false;

    public GerenciamentoHospedesController() {
        this.hospedeService = new HospedeService();
        this.listaHospedesObservavel = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        configurarTabelaHospedes();
        carregarHospedesNaTabela();
        configurarListeners();
        configurarFormatadoresDeTexto();
        limparFormulario(); 
        btnExcluirHospede.setDisable(true);
    }

    private void configurarTabelaHospedes() {
        colHospedeId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeHospede.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpfHospede.setCellValueFactory(cellData -> new SimpleStringProperty(Formatter.formatCpf(cellData.getValue().getCpf())));
        colTelefoneHospede.setCellValueFactory(cellData -> new SimpleStringProperty(Formatter.formatPhone(cellData.getValue().getTelefone())));
        colEmailHospede.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        tblHospedes.setItems(listaHospedesObservavel);
    }
    
    private void configurarListeners() {
        tblHospedes.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    hospedeSelecionadoParaEdicao = newSelection;
                    preencherFormularioComHospede(newSelection);
                    btnExcluirHospede.setDisable(false);
                } else {
                    hospedeSelecionadoParaEdicao = null;
                    btnExcluirHospede.setDisable(true);
                }
            });
    }

    private void configurarFormatadoresDeTexto() {
        txtCpfHospede.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isFormattingCpf) return;
            isFormattingCpf = true;

            String digitsOnly = newValue.replaceAll("\\D", "");
            String formattedCpf = digitsOnly;

            if (digitsOnly.length() > 9) {
                formattedCpf = String.format("%s.%s.%s-%s",
                    digitsOnly.substring(0, 3),
                    digitsOnly.substring(3, 6),
                    digitsOnly.substring(6, 9),
                    digitsOnly.substring(9));
            } else if (digitsOnly.length() > 6) {
                formattedCpf = String.format("%s.%s.%s",
                    digitsOnly.substring(0, 3),
                    digitsOnly.substring(3, 6),
                    digitsOnly.substring(6));
            } else if (digitsOnly.length() > 3) {
                formattedCpf = String.format("%s.%s",
                    digitsOnly.substring(0, 3),
                    digitsOnly.substring(3));
            }
            
            if (formattedCpf.length() > 14) {
                formattedCpf = formattedCpf.substring(0, 14);
            }

            final String finalFormattedCpf = formattedCpf;
            Platform.runLater(() -> {
                txtCpfHospede.setText(finalFormattedCpf);
                txtCpfHospede.positionCaret(finalFormattedCpf.length());
                isFormattingCpf = false;
            });
        });

        txtTelefoneHospede.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isFormattingTelefone) return;
            isFormattingTelefone = true;

            String digitsOnly = newValue.replaceAll("\\D", "");
            String formattedPhone = digitsOnly;

            if (digitsOnly.length() == 11) { // (XX) XXXXX-XXXX
                formattedPhone = String.format("(%s) %s-%s", 
                    digitsOnly.substring(0, 2), 
                    digitsOnly.substring(2, 7), 
                    digitsOnly.substring(7, 11));
            } else if (digitsOnly.length() == 10) { // (XX) XXXX-XXXX
                formattedPhone = String.format("(%s) %s-%s", 
                    digitsOnly.substring(0, 2), 
                    digitsOnly.substring(2, 6), 
                    digitsOnly.substring(6, 10));

            } else if (digitsOnly.length() > 7) {
                 if (newValue.startsWith("(") && digitsOnly.length() > 2) {
                    formattedPhone = "(" + digitsOnly.substring(0, 2) + ") " + digitsOnly.substring(2);
                 } else if (digitsOnly.length() > 2) {
                    formattedPhone = "(" + digitsOnly.substring(0, 2) + ") " + digitsOnly.substring(2);
                 }
            } else if (digitsOnly.length() > 2) {
                 formattedPhone = "(" + digitsOnly.substring(0, 2) + ") " + digitsOnly.substring(2);
            } else if (digitsOnly.length() > 0) {
                 formattedPhone = "(" + digitsOnly;
            }

            if (formattedPhone.length() > 15) {
                formattedPhone = formattedPhone.substring(0, 15);
            }
            
            final String finalFormattedPhone = formattedPhone;
            Platform.runLater(() -> {
                txtTelefoneHospede.setText(finalFormattedPhone);
                txtTelefoneHospede.positionCaret(finalFormattedPhone.length());
                isFormattingTelefone = false;
            });
        });
        if (txtCepHospede != null) {
            txtCepHospede.textProperty().addListener((observable, oldValue, newValue) -> {
                if (isFormattingCep) return;
                isFormattingCep = true;

                String digitsOnly = newValue.replaceAll("\\D", "");

                if (digitsOnly.length() > 8) {
                    digitsOnly = digitsOnly.substring(0, 8);
                }

                String formattedCep = digitsOnly;
                if (digitsOnly.length() > 5) {
                    formattedCep = digitsOnly.substring(0, 5) + "-" + digitsOnly.substring(5);
                }

                final String finalFormattedCep = formattedCep;
                Platform.runLater(() -> {
                    txtCepHospede.setText(finalFormattedCep);
                    txtCepHospede.positionCaret(finalFormattedCep.length());
                    isFormattingCep = false;
                });
            });
        }
    }

    private void carregarHospedesNaTabela() {
        List<Hospede> hospedesDoBanco = hospedeService.getAllHospedes(); 
        listaHospedesObservavel.clear();
        if (hospedesDoBanco != null) {
            listaHospedesObservavel.addAll(hospedesDoBanco);
        }
        tblHospedes.setPlaceholder(new Label("Nenhum hóspede encontrado."));
    }
    
    private void preencherFormularioComHospede(Hospede hospede) {
        txtNomeHospede.setText(hospede.getNome());
        txtCpfHospede.setText(Formatter.formatCpf(hospede.getCpf())); 
        txtTelefoneHospede.setText(Formatter.formatPhone(hospede.getTelefone()));
        txtEmailHospede.setText(hospede.getEmail());
        dpDataNascimentoHospede.setValue(hospede.getDataNascimento());

        if (hospede.getEndereco() != null && !hospede.getEndereco().isEmpty()) {
            String[] partesEndereco = hospede.getEndereco().split(",", 4);
            txtLogradouroHospede.setText(partesEndereco.length > 0 ? partesEndereco[0].trim() : "");
            txtBairroHospede.setText(partesEndereco.length > 1 ? partesEndereco[1].trim() : "");
            txtLocalidadeUfHospede.setText(partesEndereco.length > 2 ? partesEndereco[2].trim() : "");
            txtCepHospede.setText(partesEndereco.length > 3 ? partesEndereco[3].trim() : "");
        } else {
            txtLogradouroHospede.clear();
            txtBairroHospede.clear();
            txtLocalidadeUfHospede.clear();
            txtCepHospede.clear();
        }
    }

    @FXML
    private void handleSalvarHospede() {
        String nome = txtNomeHospede.getText();
        String cpfInput = txtCpfHospede.getText();
        String telefoneInput = txtTelefoneHospede.getText();
        String email = txtEmailHospede.getText();
        LocalDate dataNascimento = dpDataNascimentoHospede.getValue();

        String logradouro = txtLogradouroHospede.getText();
        String bairro = txtBairroHospede.getText();
        String localidadeUf = txtLocalidadeUfHospede.getText();
        String cep = txtCepHospede.getText();

        String enderecoCompleto = "";
        boolean enderecoPreenchido = (logradouro != null && !logradouro.trim().isEmpty()) ||
                                   (bairro != null && !bairro.trim().isEmpty()) ||
                                   (localidadeUf != null && !localidadeUf.trim().isEmpty()) ||
                                   (cep != null && !cep.trim().isEmpty());
        
        if (enderecoPreenchido) {
            enderecoCompleto = String.format("%s, %s, %s, %s",
                logradouro != null ? logradouro.trim() : "",
                bairro != null ? bairro.trim() : "",
                localidadeUf != null ? localidadeUf.trim() : "",
                cep != null ? cep.trim() : ""
            );
        }


        try {
            if (nome == null || nome.trim().isEmpty()) {
                throw new IllegalArgumentException("O nome do hóspede é obrigatório.");
            }

            String cpfLimpo = (cpfInput != null) ? cpfInput.replaceAll("[^0-9]", "") : "";
            Validator.validateCpf(cpfLimpo);

            String telefoneLimpo = (telefoneInput != null) ? telefoneInput.replaceAll("[^0-9]", "") : "";
            if (telefoneLimpo != null && !telefoneLimpo.trim().isEmpty()) {
                Validator.validateTelefone(telefoneInput); 
            }
            
            if (email != null && !email.trim().isEmpty()) {
                Validator.validateEmail(email);
            }

            if (enderecoPreenchido) {
                Validator.validateEndereco(enderecoCompleto);
            }
            
            if (dataNascimento != null) {
                Validator.validateNotFutureDate(dataNascimento);
            } else {
                 throw new IllegalArgumentException("Data de nascimento é obrigatória.");
            }


            Hospede hospede;
            boolean isAtualizacao = false;

            if (hospedeSelecionadoParaEdicao != null) {
                hospede = hospedeSelecionadoParaEdicao;
                if (!hospede.getCpf().equals(cpfLimpo)) {
                    if (hospedeService.isCpfEmUso(cpfLimpo, hospede.getId())) {
                        throw new IllegalArgumentException("O CPF informado já está cadastrado para outro hóspede.");
                    }
                }
                isAtualizacao = true;
            } else {
                if (hospedeService.isCpfEmUso(cpfLimpo, 0)) { 
                     throw new IllegalArgumentException("O CPF informado já está cadastrado.");
                }
                hospede = new Hospede();
            }

            hospede.setNome(nome.trim());
            hospede.setCpf(cpfLimpo); 
            hospede.setTelefone(telefoneInput != null ? telefoneInput.trim() : null);
            hospede.setEmail(email != null ? email.trim() : null);
            hospede.setEndereco(enderecoPreenchido ? enderecoCompleto : null);
            hospede.setDataNascimento(dataNascimento);
            
            boolean sucesso;
            if (isAtualizacao) {
                sucesso = hospedeService.updateHospede(hospede); 
            } else {
                sucesso = hospedeService.addHospede(hospede); 
            }

            if (sucesso) {
                mostrarAlerta("Sucesso", "Hóspede " + (isAtualizacao ? "atualizado" : "cadastrado") + " com sucesso!");
                carregarHospedesNaTabela();
                limparFormulario();
                tblHospedes.getSelectionModel().clearSelection();
            } else {
                mostrarAlerta("Erro", "Falha ao " + (isAtualizacao ? "atualizar" : "cadastrar") + " o hóspede.");
            }

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage());
        }
    }

    @FXML
    private void handleNovoHospede() {
        limparFormulario();
        tblHospedes.getSelectionModel().clearSelection();
        hospedeSelecionadoParaEdicao = null;
        btnExcluirHospede.setDisable(true);
        txtNomeHospede.requestFocus();
    }

    @FXML
    private void handleExcluirHospede() {
        if (hospedeSelecionadoParaEdicao == null) {
            mostrarAlerta("Ação Inválida", "Nenhum hóspede selecionado para exclusão.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, 
            "Tem certeza que deseja excluir o hóspede " + hospedeSelecionadoParaEdicao.getNome() + "?",
            ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmação de Exclusão");
        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            boolean sucesso = hospedeService.deleteHospede(hospedeSelecionadoParaEdicao.getId());
            if (sucesso) {
                mostrarAlerta("Sucesso", "Hóspede excluído com sucesso.");
                carregarHospedesNaTabela();
                limparFormulario();
            } else {
                mostrarAlerta("Erro", "Falha ao excluir o hóspede. Verifique se ele não possui dependências (ex: reservas).");
            }
        }
    }
    
    @FXML
    private void handleBuscarHospedes() {
        String termoBusca = txtBuscaHospede.getText();
        List<Hospede> hospedesFiltrados = hospedeService.findHospedeByNameOrCPF(termoBusca); 
        listaHospedesObservavel.clear();
        if (hospedesFiltrados != null) {
            listaHospedesObservavel.addAll(hospedesFiltrados);
        }
         if (listaHospedesObservavel.isEmpty()) {
            tblHospedes.setPlaceholder(new Label("Nenhum hóspede encontrado para: '" + termoBusca + "'"));
        }
    }

    private void limparFormulario() {
        txtNomeHospede.clear();
        txtCpfHospede.clear();
        txtTelefoneHospede.clear();
        txtEmailHospede.clear();
        
        txtLogradouroHospede.clear();
        txtBairroHospede.clear();
        txtLocalidadeUfHospede.clear();
        txtCepHospede.clear();
        dpDataNascimentoHospede.setValue(null);
        hospedeSelecionadoParaEdicao = null;
        if (btnExcluirHospede != null) btnExcluirHospede.setDisable(true);
        txtNomeHospede.requestFocus();
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert.AlertType tipo = Alert.AlertType.INFORMATION;
        String tituloLower = (titulo != null) ? titulo.toLowerCase() : ""; 
        if (tituloLower.contains("erro") || tituloLower.contains("falha")) {
            tipo = Alert.AlertType.ERROR;
        } else if (tituloLower.contains("aviso") || tituloLower.contains("atenção") || tituloLower.contains("inválid")) {
            tipo = Alert.AlertType.WARNING;
        }
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}