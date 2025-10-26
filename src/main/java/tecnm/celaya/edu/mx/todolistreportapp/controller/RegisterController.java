package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tecnm.celaya.edu.mx.todolistreportapp.MainApplication;
import tecnm.celaya.edu.mx.todolistreportapp.model.RegisterModel;

import java.io.IOException;

public class RegisterController {

    @FXML private StackPane rootPane;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private JFXPasswordField confirmPasswordField;
    @FXML private Label loginLink;

    private RegisterModel registerModel;

    public void initialize() {
        this.registerModel = new RegisterModel();
    }

    @FXML
    void registerUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMaterialDialog("Campos Vacíos", "Por favor, complete todos los campos.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMaterialDialog("Error de Contraseña", "Las contraseñas no coinciden.");
            return;
        }

        boolean isRegistered = registerModel.registerUser(username, password);

        if (isRegistered) {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text("Registro Exitoso"));
            content.setBody(new Text("¡Usuario registrado correctamente! Serás redirigido a la pantalla de inicio de sesión."));
            JFXDialog dialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
            JFXButton button = new JFXButton("Aceptar");
            button.setOnAction(event -> {
                dialog.close();
                try {
                    openLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            content.setActions(button);
            dialog.show();
        } else {
            showMaterialDialog("Error de Registro", "No se pudo completar el registro. El nombre de usuario puede que ya exista.");
        }
    }

    @FXML
    void openLogin() throws IOException {
        Stage stage = (Stage) loginLink.getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource("login-view.fxml"));
        stage.setScene(new Scene(root));
    }

    private void showMaterialDialog(String heading, String body) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(heading));
        content.setBody(new Text(body));
        JFXDialog dialog = new JFXDialog(rootPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton button = new JFXButton("Aceptar");
        button.setOnAction(event -> dialog.close());
        content.setActions(button);
        dialog.show();
    }
}
