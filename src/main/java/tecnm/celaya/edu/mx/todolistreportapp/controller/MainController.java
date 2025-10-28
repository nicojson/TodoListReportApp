package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tecnm.celaya.edu.mx.todolistreportapp.MainApplication;
import tecnm.celaya.edu.mx.todolistreportapp.dao.CategoriaDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.CategoriaDaoImpl;
import tecnm.celaya.edu.mx.todolistreportapp.dao.TareaDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.TareaDaoImpl;
import tecnm.celaya.edu.mx.todolistreportapp.model.Categoria;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class MainController {

    @FXML private StackPane rootPane;
    @FXML private TableView<Tarea> tasksTableView;
    @FXML private TableColumn<Tarea, String> taskNameColumn;
    @FXML private TableColumn<Tarea, String> taskDescriptionColumn;
    @FXML private TableColumn<Tarea, String> taskStatusColumn;
    @FXML private TableColumn<Tarea, String> taskCategoryColumn;
    @FXML private TableColumn<Tarea, LocalDateTime> taskDueDateColumn;
    @FXML private JFXTextField searchFilterTextField;
    @FXML private JFXComboBox<Categoria> categoryFilterComboBox;
    @FXML private Label welcomeUserLabel;

    private Usuario currentUser;
    private TareaDao tareaDao;
    private CategoriaDao categoriaDao;

    private ObservableList<Tarea> masterTaskList;
    private FilteredList<Tarea> filteredTaskList;

    @FXML
    public void initialize() {
        this.tareaDao = new TareaDaoImpl();
        this.categoriaDao = new CategoriaDaoImpl();
        this.masterTaskList = FXCollections.observableArrayList();

        this.filteredTaskList = new FilteredList<>(masterTaskList, p -> true);

        tasksTableView.setItems(filteredTaskList);

        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        taskStatusColumn.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        taskCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        taskDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));

        searchFilterTextField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        categoryFilterComboBox.valueProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    public void initData(Usuario usuario) {
        this.currentUser = usuario;
        welcomeUserLabel.setText("Bienvenido, " + currentUser.getNombreUsuario());
        loadUserTasks();
        loadFilterCategories();
    }

    private void loadUserTasks() {
        masterTaskList.clear();
        List<Tarea> userTasks = tareaDao.findByUsuarioId(currentUser.getIdUsuario());
        masterTaskList.addAll(userTasks);
    }

    private void loadFilterCategories() {
        categoryFilterComboBox.getItems().add(null);
        categoryFilterComboBox.getItems().addAll(categoriaDao.findAll());
    }

    private void applyFilters() {
        String searchText = searchFilterTextField.getText();
        Categoria selectedCategory = categoryFilterComboBox.getValue();

        Predicate<Tarea> textFilter = tarea -> {
            if (searchText == null || searchText.isEmpty()) return true;
            return tarea.getNombre().toLowerCase().contains(searchText.toLowerCase());
        };

        Predicate<Tarea> categoryFilter = tarea -> {
            if (selectedCategory == null) return true;
            if (tarea.getNombreCategoria() == null) return false;
            return tarea.getNombreCategoria().contains(selectedCategory.getNombre());
        };

        filteredTaskList.setPredicate(textFilter.and(categoryFilter));
    }

    @FXML
    void handleAddTask() {
        Tarea nuevaTarea = new Tarea();
        boolean saveClicked = showTaskEditDialog(nuevaTarea);
        if (saveClicked) {
            loadUserTasks();
        }
    }

    @FXML
    void handleEditTask() {
        if (tasksTableView.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "Sin Selección", "Ninguna Tarea Seleccionada", "Por favor, seleccione una tarea para editarla.");
            return;
        }
        Tarea selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        boolean saveClicked = showTaskEditDialog(selectedTask);
        if (saveClicked) {
            loadUserTasks();
        }
    }

    @FXML
    void handleDeleteTask() {
        if (tasksTableView.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "Sin Selección", "Ninguna Tarea Seleccionada", "Por favor, seleccione una tarea para eliminarla.");
            return;
        }
        Tarea selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("¿Está seguro de que desea eliminar la tarea?");
        alert.setContentText(selectedTask.getNombre());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = tareaDao.delete(selectedTask.getIdTarea());
            if (deleted) {
                loadUserTasks();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error de Eliminación", "No se pudo eliminar la tarea", "Ocurrió un error al intentar eliminar la tarea de la base de datos.");
            }
        }
    }

    @FXML
    void handleExport() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("export-dialog.fxml"));
            Region dialogContent = loader.load();

            JFXDialog dialog = new JFXDialog(rootPane, dialogContent, JFXDialog.DialogTransition.BOTTOM);

            ExportDialogController controller = loader.getController();
            controller.initData(dialog, filteredTaskList, rootPane);

            dialog.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleShowCharts() {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("charts-view.fxml"));
            Parent root = loader.load();

            ChartsController controller = loader.getController();
            controller.initData(currentUser);

            Stage chartsStage = new Stage();
            chartsStage.setTitle("Estadísticas de Tareas");
            chartsStage.initModality(Modality.APPLICATION_MODAL);
            chartsStage.initOwner(rootPane.getScene().getWindow());
            chartsStage.setScene(new Scene(root));
            chartsStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout() {
        try {
            Stage mainStage = (Stage) welcomeUserLabel.getScene().getWindow();
            mainStage.close();

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setTitle("TodoList App - Login");
            loginStage.setScene(new Scene(root));
            loginStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showTaskEditDialog(Tarea tarea) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("task-edit-dialog.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Tarea");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tasksTableView.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            TaskEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTask(tarea, currentUser);

            dialogStage.showAndWait();

            return controller.isSaveClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
