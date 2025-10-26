package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import tecnm.celaya.edu.mx.todolistreportapp.dao.*;
import tecnm.celaya.edu.mx.todolistreportapp.model.Categoria;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class TaskEditDialogController {

    @FXML private Label titleLabel;
    @FXML private JFXTextField nameField;
    @FXML private JFXTextArea descriptionArea;
    @FXML private JFXComboBox<String> statusComboBox;
    @FXML private CheckComboBox<Categoria> categoryCheckComboBox;
    @FXML private JFXDatePicker dueDateDatePicker;

    private Stage dialogStage;
    private Tarea tarea;
    private Usuario currentUser;
    private boolean saveClicked = false;
    private TareaDao tareaDao;
    private CategoriaDao categoriaDao;
    private TareaCategoriaDao tareaCategoriaDao;

    @FXML
    private void initialize() {
        this.tareaDao = new TareaDaoImpl();
        this.categoriaDao = new CategoriaDaoImpl();
        this.tareaCategoriaDao = new TareaCategoriaDaoImpl();

        statusComboBox.setItems(FXCollections.observableArrayList("Pendiente", "En Progreso", "Completada"));

        List<Categoria> categorias = categoriaDao.findAll();
        categoryCheckComboBox.getItems().addAll(categorias);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setTask(Tarea tarea, Usuario currentUser) {
        this.tarea = tarea;
        this.currentUser = currentUser;

        if (tarea.getIdTarea() != 0) { // Modo edición
            titleLabel.setText("Editar Tarea");
            nameField.setText(tarea.getNombre());
            descriptionArea.setText(tarea.getDescripcion());
            statusComboBox.setValue(tarea.getEstatus());
            // TODO: Cargar y seleccionar las categorías actuales de la tarea
            if (tarea.getFechaVencimiento() != null) {
                dueDateDatePicker.setValue(tarea.getFechaVencimiento().toLocalDate());
            }
        } else { // Modo añadir
            titleLabel.setText("Añadir Nueva Tarea");
            statusComboBox.setValue("Pendiente");
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            tarea.setNombre(nameField.getText());
            tarea.setDescripcion(descriptionArea.getText());
            tarea.setEstatus(statusComboBox.getValue());
            if (dueDateDatePicker.getValue() != null) {
                tarea.setFechaVencimiento(dueDateDatePicker.getValue().atTime(LocalTime.MIDNIGHT));
            }

            boolean success;
            if (tarea.getIdTarea() == 0) { // Añadir nueva tarea
                tarea.setIdUsuario(currentUser.getIdUsuario());
                success = tareaDao.save(tarea);
            } else { // Actualizar tarea existente
                success = tareaDao.update(tarea);
            }

            if (success) {
                // Obtener la lista de categorías seleccionadas
                List<Categoria> selectedCategories = categoryCheckComboBox.getCheckModel().getCheckedItems();
                
                // Primero, eliminar todas las asociaciones viejas
                tareaCategoriaDao.disassociateByTareaId(tarea.getIdTarea());
                
                // Luego, crear las nuevas asociaciones
                for (Categoria cat : selectedCategories) {
                    tareaCategoriaDao.associate(tarea.getIdTarea(), cat.getIdCategoria());
                }

                saveClicked = true;
                dialogStage.close();
            }
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        // ... (validaciones)
        return true;
    }
}
