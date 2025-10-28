package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import tecnm.celaya.edu.mx.todolistreportapp.dao.CategoriaDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.CategoriaDaoImpl;
import tecnm.celaya.edu.mx.todolistreportapp.model.Categoria;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;
import tecnm.celaya.edu.mx.todolistreportapp.service.ChartService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartsController {

    @FXML private JFXComboBox<String> timeFilterComboBox;
    @FXML private PieChart statusPieChart;
    @FXML private BarChart<String, Number> categoryBarChart;
    @FXML private TextArea diagnosticTextArea;

    private Usuario currentUser;
    private ChartService chartService;
    private CategoriaDao categoriaDao;
    private XYChart.Series<String, Number> categorySeries;
    private List<String> allCategoryNames;

    @FXML
    public void initialize() {
        this.chartService = new ChartService();
        this.categoriaDao = new CategoriaDaoImpl();

        timeFilterComboBox.setItems(FXCollections.observableArrayList("Semana", "Mes", "Año", "Todo"));
        timeFilterComboBox.setValue("Mes");

        // 1. Obtener todas las categorías una sola vez
        this.allCategoryNames = categoriaDao.findAll().stream()
                .map(Categoria::getNombre)
                .collect(Collectors.toList());

        // 2. Configurar el eje X con todas las categorías
        CategoryAxis xAxis = (CategoryAxis) categoryBarChart.getXAxis();
        xAxis.setCategories(FXCollections.observableArrayList(allCategoryNames));

        // 3. Crear la serie de datos una sola vez y añadirla al gráfico
        this.categorySeries = new XYChart.Series<>();
        categorySeries.setName("Tareas por Categoría");
        categoryBarChart.getData().add(categorySeries);

        timeFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadChartData();
            }
        });
    }

    public void initData(Usuario usuario) {
        this.currentUser = usuario;
        loadChartData();
    }

    private void loadChartData() {
        String period = timeFilterComboBox.getValue();

        // --- Lógica para el Gráfico de Estado (sin cambios) ---
        Map<String, Integer> statusData = chartService.getDetailedStatusDataForPeriod(currentUser.getIdUsuario(), period);
        int completedCount = statusData.getOrDefault("Completada", 0);
        int notCompletedCount = statusData.getOrDefault("En Progreso", 0) + statusData.getOrDefault("Pendiente", 0);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        if (completedCount > 0 || notCompletedCount > 0) {
            pieChartData.add(new PieChart.Data("Completadas (" + completedCount + ")", completedCount));
            pieChartData.add(new PieChart.Data("No Completadas (" + notCompletedCount + ")", notCompletedCount));
        }
        statusPieChart.setData(pieChartData);

        // --- Lógica para el Gráfico de Categorías (NUEVA LÓGICA) ---
        Map<String, Integer> categoryData = chartService.getCategoryDataForPeriod(currentUser.getIdUsuario(), period);
        diagnosticTextArea.setText("Datos recibidos del servicio para el gráfico de barras:\n" + categoryData.toString());

        // 4. Limpiar los datos de la serie existente
        categorySeries.getData().clear();

        // 5. Poblar la serie, asegurando que cada categoría esté presente
        for (String categoryName : allCategoryNames) {
            int count = categoryData.getOrDefault(categoryName, 0);
            categorySeries.getData().add(new XYChart.Data<>(categoryName, count));
        }
    }
}
