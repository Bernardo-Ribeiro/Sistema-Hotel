package com.hotel.gerenciador.controller;

import com.hotel.gerenciador.model.Funcionario;
import com.hotel.gerenciador.service.FuncionarioService;
import com.hotel.gerenciador.util.Formatter;
import com.hotel.gerenciador.util.Validator;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.text.NumberFormat;
import java.text.ParseException; 
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

public class GerenciamentoFuncionariosController extends BaseController {

    @FXML private TextField txtNomeFuncionario;
    @FXML private TextField txtCpfFuncionario;
    @FXML private TextField txtTelefoneFuncionario;
    @FXML private TextField txtEmailFuncionario;
    @FXML private TextField txtCargoFuncionario;
    @FXML private TextField txtSalarioFuncionario;
    @FXML private TextField txtEnderecoFuncionario;
    @FXML private DatePicker dpDataAdmissaoFuncionario;

    @FXML private Button btnSalvarFuncionario;
    @FXML private Button btnNovoFuncionario;
    @FXML private Button btnDemitirFuncionario;
    @FXML private Button btnRegistrarPagamento;

    @FXML private TextField txtBuscaFuncionario;
    @FXML private Button btnBuscarFuncionarios;

    @FXML private TableView<Funcionario> tblFuncionarios;
    @FXML private TableColumn<Funcionario, Integer> colFuncionarioId;
    @FXML private TableColumn<Funcionario, String> colNomeFuncionario;
    @FXML private TableColumn<Funcionario, String> colCargoFuncionario;
    @FXML private TableColumn<Funcionario, String> colSalarioFuncionario;
    @FXML private TableColumn<Funcionario, String> colCpfFuncionario;
    @FXML private TableColumn<Funcionario, String> colTelefoneFuncionario;
    @FXML private TableColumn<Funcionario, String> colDataAdmissaoFuncionario;
    @FXML private TableColumn<Funcionario, String> colEmailFuncionario;

    private FuncionarioService funcionarioService;
    private ObservableList<Funcionario> listaFuncionariosObservavel;
    private Funcionario funcionarioSelecionadoParaEdicao = null;

    private boolean isFormattingCpf = false;
    private boolean isFormattingTelefone = false;
    private boolean isFormattingSalario = false;
    
    private static final Pattern NON_NUMERIC = Pattern.compile("[^0-9]");
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));

    public GerenciamentoFuncionariosController() {
        this.funcionarioService = new FuncionarioService();
        this.listaFuncionariosObservavel = FXCollections.observableArrayList();
    }

    @FXML
    private void initialize() {
        configurarTabelaFuncionarios();
        carregarFuncionariosNaTabela();
        configurarListenersDeSelecao();
        configurarFormatadoresDeTexto();
        limparFormulario();
    }

    private void configurarTabelaFuncionarios() {
        colFuncionarioId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNomeFuncionario.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCargoFuncionario.setCellValueFactory(new PropertyValueFactory<>("cargo"));
        
        colSalarioFuncionario.setCellValueFactory(cellData -> {
            double salario = cellData.getValue().getSalario();
            return new SimpleStringProperty(Formatter.formatCurrency(salario));
        });
        colCpfFuncionario.setCellValueFactory(cellData -> 
            new SimpleStringProperty(Formatter.formatCpf(cellData.getValue().getCpf())));
        colTelefoneFuncionario.setCellValueFactory(cellData -> 
            new SimpleStringProperty(Formatter.formatPhone(cellData.getValue().getTelefone())));
        colDataAdmissaoFuncionario.setCellValueFactory(cellData -> 
            new SimpleStringProperty(Formatter.formatDate(cellData.getValue().getDataAdmissao())));
        colEmailFuncionario.setCellValueFactory(new PropertyValueFactory<>("email"));
        
        tblFuncionarios.setItems(listaFuncionariosObservavel);
    }

    private void configurarListenersDeSelecao() {
        tblFuncionarios.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    funcionarioSelecionadoParaEdicao = newSelection;
                    preencherFormularioComFuncionario(newSelection);
                    btnDemitirFuncionario.setDisable(false);
                    btnRegistrarPagamento.setDisable(false);
                } else {
                    funcionarioSelecionadoParaEdicao = null;
                    limparFormulario();
                    btnDemitirFuncionario.setDisable(true);
                    btnRegistrarPagamento.setDisable(true);
                }
            });
    }
    
    private void configurarFormatadoresDeTexto() {
        txtCpfFuncionario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isFormattingCpf) return;
            isFormattingCpf = true;

            String digitsOnly = newValue.replaceAll("\\D", "");
            String formattedCpf = digitsOnly; 

            if (digitsOnly.length() > 11) {
                digitsOnly = digitsOnly.substring(0, 11);
            }

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
            
            final String finalFormattedCpf = formattedCpf;
            Platform.runLater(() -> {
                txtCpfFuncionario.setText(finalFormattedCpf);
                txtCpfFuncionario.positionCaret(finalFormattedCpf.length());
                isFormattingCpf = false;
            });
        });

        txtTelefoneFuncionario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isFormattingTelefone) return;
            isFormattingTelefone = true;

            String digitsOnly = newValue.replaceAll("\\D", "");
            String formattedPhone = digitsOnly;

            if (digitsOnly.length() > 11) {
                digitsOnly = digitsOnly.substring(0, 11);
            }

            if (digitsOnly.length() == 11) { // (XX) XXXXX-XXXX
                formattedPhone = String.format("(%s) %s-%s", 
                    digitsOnly.substring(0, 2), 
                    digitsOnly.substring(2, 7), 
                    digitsOnly.substring(7, 11));
            } else if (digitsOnly.length() > 6 && digitsOnly.length() <= 10 && newValue.matches("^\\(\\d{2}\\)\\s\\d{4,5}$")) {
                 formattedPhone = newValue;
            } else if (digitsOnly.length() == 10) { // (XX) XXXX-XXXX
                formattedPhone = String.format("(%s) %s-%s", 
                    digitsOnly.substring(0, 2), 
                    digitsOnly.substring(2, 6), 
                    digitsOnly.substring(6, 10));
            } else if (digitsOnly.length() > 7) { 
                formattedPhone = String.format("(%s) %s-%s",
                    digitsOnly.substring(0, 2),
                    digitsOnly.substring(2, (digitsOnly.length() == 11 || digitsOnly.length() >= 7 ? 7 : 6)),
                    digitsOnly.substring((digitsOnly.length() == 11 || digitsOnly.length() >= 7 ? 7 : 6)));
            } else if (digitsOnly.length() > 2) {
                 formattedPhone = String.format("(%s) %s", digitsOnly.substring(0, 2), digitsOnly.substring(2));
            } else if (digitsOnly.length() > 0) {
                 formattedPhone = "(" + digitsOnly;
            }
            
            final String finalFormattedPhone = formattedPhone;
            Platform.runLater(() -> {
                txtTelefoneFuncionario.setText(finalFormattedPhone);
                txtTelefoneFuncionario.positionCaret(finalFormattedPhone.length());
                isFormattingTelefone = false;
            });
        });
        
        txtSalarioFuncionario.textProperty().addListener((observable, oldValue, newValue) -> {
            if (isFormattingSalario) return;
            isFormattingSalario = true;

            String digits = newValue.replaceAll("[^\\d]", "");
            String formattedText = "";

            if (!digits.isEmpty()) {
                try {
                    double value = Double.parseDouble(digits) / 100.0;
                    formattedText = currencyFormat.format(value);
                } catch (NumberFormatException e) {
                    try {
                         Number parsedNum = currencyFormat.parse(newValue);
                         formattedText = currencyFormat.format(parsedNum.doubleValue());
                    } catch (ParseException pe) {
                       formattedText = "";
                    }
                }
            }
            
            final String finalText = formattedText;
            Platform.runLater(() -> {
                txtSalarioFuncionario.setText(finalText);
                txtSalarioFuncionario.positionCaret(finalText.length());
                isFormattingSalario = false;
            });
        });
    }

    private void carregarFuncionariosNaTabela() {
        List<Funcionario> funcionariosDoBanco = funcionarioService.getAllFuncionarios();
        listaFuncionariosObservavel.clear();
        if (funcionariosDoBanco != null) {
            listaFuncionariosObservavel.addAll(funcionariosDoBanco);
        }
        tblFuncionarios.setPlaceholder(new Label("Nenhum funcionário cadastrado."));
    }
    
    private void preencherFormularioComFuncionario(Funcionario funcionario) {
        txtNomeFuncionario.setText(funcionario.getNome());
        txtCpfFuncionario.setText(Formatter.formatCpf(funcionario.getCpf())); 
        txtTelefoneFuncionario.setText(Formatter.formatPhone(funcionario.getTelefone()));
        txtEmailFuncionario.setText(funcionario.getEmail());
        txtCargoFuncionario.setText(funcionario.getCargo());
        txtSalarioFuncionario.setText(currencyFormat.format(funcionario.getSalario()));
        txtEnderecoFuncionario.setText(funcionario.getEndereco());
        dpDataAdmissaoFuncionario.setValue(funcionario.getDataAdmissao());
    }

    @FXML
    private void handleSalvarFuncionario() {
        try {
            String nome = txtNomeFuncionario.getText();
            String cpfInput = txtCpfFuncionario.getText();
            String telefoneInput = txtTelefoneFuncionario.getText();
            String email = txtEmailFuncionario.getText();
            String cargo = txtCargoFuncionario.getText();
            String salarioInput = txtSalarioFuncionario.getText();
            String endereco = txtEnderecoFuncionario.getText();
            LocalDate dataAdmissao = dpDataAdmissaoFuncionario.getValue();

            // Validações usando o Validator
            Validator.validateNotEmpty(nome, "Nome do funcionário");
            Validator.validateNotEmpty(cargo, "Cargo do funcionário");
            
            String cpfLimpo = NON_NUMERIC.matcher(cpfInput).replaceAll("");
            Validator.validateCpf(cpfLimpo);
            
            String telefoneLimpo = NON_NUMERIC.matcher(telefoneInput).replaceAll("");
            Validator.validateTelefone(telefoneLimpo);
            
            if (email != null && !email.trim().isEmpty()) {
                Validator.validateEmail(email);
            }
            
            double salario = 0.0;
            if (salarioInput != null && !salarioInput.isEmpty()) {
                try {
                    Number parsedNumber = currencyFormat.parse(salarioInput);
                    salario = parsedNumber.doubleValue();
                } catch (ParseException e) {
                    throw new IllegalArgumentException("Formato de salário inválido. Ex: R$ 1.234,56");
                }
            }
            Validator.validatePositiveValue(salario);
            
            if (endereco != null && !endereco.trim().isEmpty()) {
                Validator.validateEndereco(endereco);
            } else {
                throw new IllegalArgumentException("Endereço é obrigatório.");
            }
            
            if (dataAdmissao != null) {
                Validator.validateNotFutureDate(dataAdmissao);
            } else {
                throw new IllegalArgumentException("Data de admissão é obrigatória.");
            }
            
            // Criar ou atualizar funcionário
            Funcionario funcionario = funcionarioSelecionadoParaEdicao != null ? 
                funcionarioSelecionadoParaEdicao : new Funcionario();
            
            funcionario.setNome(nome.trim());
            funcionario.setCargo(cargo.trim());
            funcionario.setSalario(salario);
            funcionario.setTelefone(telefoneLimpo); 
            funcionario.setCpf(cpfLimpo); 
            funcionario.setEmail(email != null ? email.trim() : null);
            funcionario.setEndereco(endereco.trim());
            funcionario.setDataAdmissao(dataAdmissao);
            
            if (funcionarioSelecionadoParaEdicao == null) {
                funcionarioService.addFuncionario(funcionario);
                mostrarAlerta("Sucesso", "Funcionário cadastrado com sucesso!");
            } else {
                funcionarioService.upFuncionario(funcionario);
                mostrarAlerta("Sucesso", "Funcionário atualizado com sucesso!");
            }
            
            limparFormulario();
            carregarFuncionariosNaTabela();
            
        } catch (IllegalArgumentException e) {
            mostrarAlerta("Erro de Validação", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta("Erro", "Ocorreu um erro ao salvar o funcionário: " + e.getMessage());
        }
    }

    @FXML
    private void handleNovoFuncionario() {
        limparFormulario();
        txtNomeFuncionario.requestFocus();
    }

    @FXML
    private void handleDemitirFuncionario() {
        if (funcionarioSelecionadoParaEdicao == null) {
            mostrarAlerta("Ação Inválida", "Nenhum funcionário selecionado para demissão.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, 
            "Tem certeza que deseja demitir o funcionário " + funcionarioSelecionadoParaEdicao.getNome() + "?\nEsta ação não pode ser desfeita.",
            ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmação de Demissão");
        Optional<ButtonType> resultado = confirmacao.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.YES) {
            boolean sucesso = funcionarioService.delFuncionario(funcionarioSelecionadoParaEdicao.getId());
            if (sucesso) {
                mostrarAlerta("Sucesso", "Funcionário demitido com sucesso.");
                carregarFuncionariosNaTabela();
                limparFormulario();
            } else {
                mostrarAlerta("Erro", "Falha ao demitir o funcionário. Verifique se ele não possui dependências ativas.");
            }
        }
    }
    
    @FXML
    private void handleBuscarFuncionarios() {
        String termoBusca = txtBuscaFuncionario.getText().trim();
        List<Funcionario> funcionariosFiltrados;

        if (termoBusca.isEmpty()) {
            funcionariosFiltrados = funcionarioService.getAllFuncionarios();
        } else {
            funcionariosFiltrados = funcionarioService.findFuncionariosPorNome(termoBusca);
            if (funcionariosFiltrados.isEmpty()) {
                Funcionario porCpf = funcionarioService.findFuncionarioPorCpf(NON_NUMERIC.matcher(termoBusca).replaceAll(""));
                if (porCpf != null) {
                    funcionariosFiltrados.add(porCpf);
                }
            }
        }
        
        listaFuncionariosObservavel.clear();
        if (funcionariosFiltrados != null && !funcionariosFiltrados.isEmpty()) {
            listaFuncionariosObservavel.addAll(funcionariosFiltrados);
        }
         if (listaFuncionariosObservavel.isEmpty()) {
            tblFuncionarios.setPlaceholder(new Label("Nenhum funcionário encontrado para: '" + termoBusca + "'"));
        } else {
             tblFuncionarios.setPlaceholder(new Label("Nenhum funcionário cadastrado."));
         }
    }

    @FXML
    private void handleRegistrarPagamento() {
        if (funcionarioSelecionadoParaEdicao == null) {
            mostrarAlerta("Ação Inválida", "Selecione um funcionário para registrar o pagamento.");
            return;
        }

        TextInputDialog dialogValor = new TextInputDialog("0,00");
        dialogValor.setTitle("Registrar Pagamento de Salário");
        dialogValor.setHeaderText("Registrar Pagamento para: " + funcionarioSelecionadoParaEdicao.getNome());
        dialogValor.setContentText("Valor do Pagamento (R$):");
        Optional<String> valorResult = dialogValor.showAndWait();

        if (valorResult.isPresent() && !valorResult.get().isEmpty()) {
            try {
                Number parsedNumber = currencyFormat.parse(valorResult.get());
                double valor = parsedNumber.doubleValue();

                DatePickerDialog dialogData = new DatePickerDialog(LocalDate.now());
                dialogData.setTitle("Data do Pagamento");
                dialogData.setHeaderText("Selecione a data do pagamento:");
                Optional<LocalDate> dataResult = dialogData.showAndWait();

                if (dataResult.isPresent()) {
                    LocalDate dataPagamento = dataResult.get();

                    TextInputDialog dialogMesRef = new TextInputDialog(LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MM/yyyy")));
                    dialogMesRef.setTitle("Mês de Referência");
                    dialogMesRef.setHeaderText("Mês de Referência do Pagamento (MM/YYYY):");
                    dialogMesRef.setContentText("Mês/Ano:");
                    Optional<String> mesRefResult = dialogMesRef.showAndWait();

                    if (mesRefResult.isPresent() && !mesRefResult.get().isEmpty()) {
                        String mesReferencia = mesRefResult.get();
                        
                        System.out.println("Pagamento a ser registrado (placeholder):");
                        System.out.println("Funcionário ID: " + funcionarioSelecionadoParaEdicao.getId());
                        System.out.println("Valor: " + valor);
                        System.out.println("Data Pagamento: " + dataPagamento);
                        System.out.println("Mês Referência: " + mesReferencia);
                        
                        // boolean sucesso = new PagamentoService().registrarPagamentoSalario(funcionarioId, valor, dataPagamento, mesReferencia);
                        // For now, just simulate success
                        boolean sucesso = true; 
                        
                        if (sucesso) {
                             mostrarAlerta("Pagamento", "Registro de pagamento (simulado) processado.");
                        } else {
                             mostrarAlerta("Pagamento", "Falha ao processar registro de pagamento (simulado).");
                        }
                    }
                }
            } catch (ParseException e) {
                mostrarAlerta("Erro de Entrada", "Valor do pagamento inválido. Use o formato R$ 1.234,56.");
            }
        }
    }


    private void limparFormulario() {
        txtNomeFuncionario.clear();
        txtCpfFuncionario.clear();
        txtTelefoneFuncionario.clear();
        txtEmailFuncionario.clear();
        txtCargoFuncionario.clear();
        txtSalarioFuncionario.clear();
        txtEnderecoFuncionario.clear();
        dpDataAdmissaoFuncionario.setValue(null);
        
        funcionarioSelecionadoParaEdicao = null;
        tblFuncionarios.getSelectionModel().clearSelection(); 
        
        if (btnDemitirFuncionario != null) btnDemitirFuncionario.setDisable(true);
        if (btnRegistrarPagamento != null) btnRegistrarPagamento.setDisable(true);

    }

    private static class DatePickerDialog extends Dialog<LocalDate> {
        public DatePickerDialog(LocalDate initialDate) {
            setTitle("Selecione a Data");
            DatePicker datePicker = new DatePicker(initialDate);
            getDialogPane().setContent(datePicker);
            getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    return datePicker.getValue();
                }
                return null;
            });
        }
    }
}