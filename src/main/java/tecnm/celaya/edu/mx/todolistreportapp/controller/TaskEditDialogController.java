package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tecnm.celaya.edu.mx.todolistreportapp.dao.*;
import tecnm.celaya.edu.mx.todolistreportapp.model.Categoria;
import tecnm.celaya.edu.mx.todolistreportapp.model.Tarea;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.time.LocalTime;
import java.util.List;

public class TaskEditDialogController {

    @FXML private Label titleLabel;
    @FXML private JFXTextField nameField;
    @FXML private JFXTextArea descriptionArea;
    @FXML private JFXComboBox<String> statusComboBox;
    @FXML private JFXComboBox<Categoria> categoryComboBox;
    @FXML private JFXDatePicker dueDateDatePicker;

    private Stage dialogStage;
    private Tarea tarea;
    private Usuario currentUser;
    private boolean saveClicked = false;
    private TareaDao tareaDao;
    private CategoriaDao categoriaDao;
    private TareaCategoriaDao tareaCategoriaDao; // Nuevo DAO

    @FXML
    private void initialize() {
        this.tareaDao = new TareaDaoImpl();
        this.categoriaDao = new CategoriaDaoImpl();
        this.tareaCategoriaDao = new TareaCategoriaDaoImpl(); // Inicializar el nuevo DAO

        statusComboBox.setItems(FXCollections.observableArrayList("Pendiente", "En Progreso", "Completada"));

        List<Categoria> categorias = categoriaDao.findAll();
        categoryComboBox.setItems(FXCollections.observableArrayList(categorias));
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
            // TODO: Cargar y seleccionar la categoría actual de la tarea
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
                // Asociar la categoría seleccionada
                Categoria selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
                if (selectedCategory != null) {
                    // ¡Aquí está la corrección! Llamamos al nuevo DAO.
                    tareaCategoriaDao.associate(tarea.getIdTarea(), selectedCategory.getIdCategoria());
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
