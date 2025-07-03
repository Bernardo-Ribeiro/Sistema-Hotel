package com.hotel.gerenciador.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

import com.hotel.gerenciador.models.*;
import com.hotel.gerenciador.services.*;
import com.hotel.gerenciador.utils.StatusQuarto;
import com.hotel.gerenciador.utils.StatusReserva;
import com.hotel.gerenciador.utils.TipoQuarto;
import com.hotel.gerenciador.utils.CalculadoraFinanceira;

public class DashboardController {

    @FXML private BorderPane dashboardPane;
    @FXML private Label lblTaxaOcupacao;
    @FXML private Label lblReceitaMes;
    @FXML private Label lblHospedesAtuais;
    @FXML private PieChart graficoOcupacaoQuartos;
    @FXML private LineChart<String, Number> graficoReceita;
    @FXML private BarChart<String, Number> graficoCheckInsOuts;
    @FXML private BarChart<String, Number> graficoConsumos;

    private final QuartoService quartoService;
    private final ReservaService reservaService;
    private final PagamentoService pagamentoService;
    private final ConsumoService consumoService;
    private final ConsumoServicosService consumoServicosService;
    private final CalculadoraFinanceira calculadoraFinanceira;

    private Map<String, BigDecimal> receitaMensalCalculada;

    public DashboardController() {
        this.quartoService = new QuartoService();
        this.reservaService = new ReservaService();
        this.pagamentoService = new PagamentoService();
        this.consumoService = new ConsumoService();
        this.consumoServicosService = new ConsumoServicosService();
        this.calculadoraFinanceira = new CalculadoraFinanceira(
            pagamentoService, consumoService, consumoServicosService);
    }

    @FXML
    private void initialize() {
        carregarEProcessarDadosReceita();

        atualizarCards();
        atualizarGraficoOcupacao();
        atualizarGraficoReceita();
        atualizarGraficoCheckInsOuts();
        atualizarGraficoConsumos();
    }
    private void carregarEProcessarDadosReceita() {
        this.receitaMensalCalculada = new LinkedHashMap<>();
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yy");

        for (int i = 5; i >= 0; i--) {
            LocalDate mes = hoje.minusMonths(i);
            LocalDate inicioMes = mes.withDayOfMonth(1);
            LocalDate fimMes = mes.withDayOfMonth(mes.lengthOfMonth());
            
            List<Reserva> reservasMes = reservaService.buscarReservasComFiltros(null, inicioMes, fimMes, null)
                .stream()
                .filter(r -> r.getStatus() == StatusReserva.HOSPEDADO || r.getStatus() == StatusReserva.CONCLUIDA)
                .collect(Collectors.toList());

            BigDecimal receitaDoMes = BigDecimal.ZERO;
            for (Reserva reserva : reservasMes) {
                receitaDoMes = receitaDoMes.add(calculadoraFinanceira.calcularTotalPagoParaReserva(reserva.getId()));
            }
            
            receitaMensalCalculada.put(mes.format(formatter), receitaDoMes);
        }
    }

    private void atualizarCards() {
        List<Quarto> todosQuartos = quartoService.findAll();
        long quartosOcupados = todosQuartos.stream()
            .filter(q -> q.getStatus() == StatusQuarto.OCUPADO)
            .count();
        double taxaOcupacao = todosQuartos.isEmpty() ? 0 : (quartosOcupados * 100.0 / todosQuartos.size());
        lblTaxaOcupacao.setText(String.format("%.1f%%", taxaOcupacao));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM/yy");
        String mesAtualFormatado = LocalDate.now().format(formatter);
        BigDecimal receitaMes = receitaMensalCalculada.getOrDefault(mesAtualFormatado, BigDecimal.ZERO);
        lblReceitaMes.setText(String.format("R$ %.2f", receitaMes));

        long hospedesAtuais = reservaService.findReservasPorStatus(StatusReserva.HOSPEDADO).size();
        lblHospedesAtuais.setText(String.valueOf(hospedesAtuais));
    }

    private void atualizarGraficoReceita() {
        XYChart.Series<String, Number> serieReceita = new XYChart.Series<>();
        serieReceita.setName("Receita Mensal");

        receitaMensalCalculada.forEach((mesFormatado, receita) -> {
            double receitaGrafico = receita.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            serieReceita.getData().add(new XYChart.Data<>(mesFormatado, receitaGrafico));
        });

        graficoReceita.setData(FXCollections.observableArrayList(serieReceita));
    }

    private void atualizarGraficoOcupacao() {
        List<Quarto> todosQuartos = quartoService.findAll();
        Map<TipoQuarto, Long> ocupacaoPorTipo = todosQuartos.stream()
            .filter(q -> q.getStatus() == StatusQuarto.OCUPADO)
            .collect(Collectors.groupingBy(Quarto::getTipo, Collectors.counting()));
        ObservableList<PieChart.Data> dados = FXCollections.observableArrayList();
        ocupacaoPorTipo.forEach((tipo, quantidade) -> 
            dados.add(new PieChart.Data(tipo.toString(), quantidade)));
        graficoOcupacaoQuartos.setData(dados);
        graficoOcupacaoQuartos.setTitle("Ocupação por Tipo de Quarto");
    }

    private void atualizarGraficoCheckInsOuts() {
        LocalDate hoje = LocalDate.now();
        ObservableList<XYChart.Series<String, Number>> series = FXCollections.observableArrayList();
        XYChart.Series<String, Number> serieCheckIns = new XYChart.Series<>();
        serieCheckIns.setName("Check-ins");
        XYChart.Series<String, Number> serieCheckOuts = new XYChart.Series<>();
        serieCheckOuts.setName("Check-outs");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
        for (int i = 0; i < 7; i++) {
            LocalDate dia = hoje.plusDays(i);
            List<Reserva> checkInsDoDia = reservaService.buscarReservasComFiltros(StatusReserva.CONFIRMADA, dia, dia, null)
                .stream()
                .filter(r -> r.getDataCheckIn().isEqual(dia))
                .collect(Collectors.toList());
            List<Reserva> checkOutsDoDia = reservaService.buscarReservasComFiltros(StatusReserva.HOSPEDADO, dia, dia, null)
                .stream()
                .filter(r -> r.getDataCheckOut().isEqual(dia))
                .collect(Collectors.toList());
            serieCheckIns.getData().add(new XYChart.Data<>(dia.format(formatter), checkInsDoDia.size()));
            serieCheckOuts.getData().add(new XYChart.Data<>(dia.format(formatter), checkOutsDoDia.size()));
        }
        series.addAll(serieCheckIns, serieCheckOuts);
        graficoCheckInsOuts.setData(series);
    }

    private void atualizarGraficoConsumos() {
        graficoConsumos.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Quantidade Consumida");
        ConsumoService consumoService = new ConsumoService();
        ProdutoService produtoService = new ProdutoService();
        List<Consumo> consumos = consumoService.getAllConsumos();
        if (consumos != null) {
            Map<Integer, Integer> quantidadePorProduto = new HashMap<>();
            for (Consumo consumo : consumos) {
                quantidadePorProduto.merge(consumo.getIdProduto(), consumo.getQuantidade(), Integer::sum);
            }
            quantidadePorProduto.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    Produto produto = produtoService.findProdutoPorId(entry.getKey());
                    if (produto != null) {
                        series.getData().add(new XYChart.Data<>(produto.getNome(), entry.getValue()));
                    }
                });
        }
        graficoConsumos.getData().add(series);
    }

    @FXML
    private void abrirDashboard() {
        loadView("/views/Dashboard.fxml");
    }

    @FXML
    private void abrirMapaQuartos() {
        loadView("/views/MapaQuartos.fxml");
    }

    @FXML
    private void abrirReservas() {
        loadView("/views/Reservas.fxml");
    }

    @FXML
    private void abrirCheckIn() {
        loadView("/views/CheckIn.fxml");
    }

    @FXML
    private void abrirGerenciamentoHospedes() {
        loadView("/views/GerenciamentoHospedes.fxml");
    }

    @FXML
    private void abrirFuncionarios() {
        loadView("/views/GerenciamentoFuncionario.fxml");
    }

    @FXML
    private void abrirManutencao() {
        loadView("/views/GerenciamentoManutencao.fxml");
    }

    @FXML
    private void abrirRelatorios() {
        loadView("/views/Relatorios.fxml");
    }

    @FXML
    private void sair() {
        System.exit(0);
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            dashboardPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao Carregar Tela");
            alert.setContentText("Não foi possível carregar a tela: " + fxmlPath);
            alert.showAndWait();
        }
    }
}