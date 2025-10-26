package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tecnm.celaya.edu.mx.todolistreportapp.MainApplication;
import tecnm.celaya.edu.mx.todolistreportapp.dao.TareaDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.TareaDaoImpl;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class MainController {

    @FXML private Label welcomeUserLabel;
    @FXML private TableView<Tarea> tasksTableView;
    @FXML private TableColumn<Tarea, String> taskNameColumn;
    @FXML private TableColumn<Tarea, String> taskDescriptionColumn;
    @FXML private TableColumn<Tarea, String> taskStatusColumn;
    @FXML private TableColumn<Tarea, String> taskCategoryColumn;
    @FXML private TableColumn<Tarea, LocalDateTime> taskDueDateColumn;

    private Usuario currentUser;
    private TareaDao tareaDao;
    private ObservableList<Tarea> tareasList;

    @FXML
    public void initialize() {
        this.tareaDao = new TareaDaoImpl();
        this.tareasList = FXCollections.observableArrayList();

        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        taskDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        taskStatusColumn.setCellValueFactory(new PropertyValueFactory<>("estatus"));
        taskCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("nombreCategoria"));
        taskDueDateColumn.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));

        tasksTableView.setItems(tareasList);
    }

    public void initData(Usuario usuario) {
        this.currentUser = usuario;
        if (currentUser != null) {
            welcomeUserLabel.setText("Bienvenido, " + currentUser.getNombreUsuario());
            loadUserTasks();
        } else {
            System.err.println("MainController: Error, el usuario recibido es NULL.");
        }
    }

    private void loadUserTasks() {
        tareasList.clear();
        List<Tarea> userTasks = tareaDao.findByUsuarioId(currentUser.getIdUsuario());
        tareasList.addAll(userTasks);
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
        Tarea selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            boolean saveClicked = showTaskEditDialog(selectedTask);
            if (saveClicked) {
                loadUserTasks();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Sin Selección", "Ninguna Tarea Seleccionada", "Por favor, seleccione una tarea en la tabla para editarla.");
        }
    }

    @FXML
    void handleDeleteTask() {
        Tarea selectedTask = tasksTableView.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Eliminación");
            alert.setHeaderText("¿Está seguro de que desea eliminar la tarea?");
            alert.setContentText(selectedTask.getNombre());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean deleted = tareaDao.delete(selectedTask.getIdTarea());
                if (deleted) {
                    loadUserTasks(); // Recargar la tabla
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error de Eliminación", "No se pudo eliminar la tarea", "Ocurrió un error al intentar eliminar la tarea de la base de datos.");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Sin Selección", "Ninguna Tarea Seleccionada", "Por favor, seleccione una tarea en la tabla para eliminarla.");
        }
    }

    @FXML
    void logout() {
        try {
            // Obtener la ventana actual
            Stage currentStage = (Stage) welcomeUserLabel.getScene().getWindow();

            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("login-view.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena y mostrarla
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("TodoList App - Login");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error de Navegación", "No se pudo cargar la pantalla de login", "Ocurrió un error inesperado al intentar cerrar la sesión.");
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
