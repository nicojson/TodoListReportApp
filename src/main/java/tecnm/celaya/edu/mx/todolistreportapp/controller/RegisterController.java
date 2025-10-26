package tecnm.celaya.edu.mx.todolistreportapp.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tecnm.celaya.edu.mx.todolistreportapp.MainApplication;
import tecnm.celaya.edu.mx.todolistreportapp.model.RegisterModel;

import java.io.IOException;

public class RegisterController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private JFXPasswordField confirmPasswordField;

    @FXML
    private Label loginLink;

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
            System.out.println("Por favor, complete todos los campos.");
            // TODO: Mostrar alerta visual al usuario
            return;
        }

        if (!password.equals(confirmPassword)) {
            System.out.println("Error: Las contraseñas no coinciden.");
            // TODO: Mostrar alerta visual al usuario
            return;
        }

        boolean isRegistered = registerModel.registerUser(username, password);

        if (isRegistered) {
            System.out.println("¡Registro exitoso!");
            // TODO: Navegar a la pantalla de login o directamente a la pantalla principal
            try {
                openLogin();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error: No se pudo completar el registro. El usuario puede que ya exista.");
            // TODO: Mostrar alerta visual al usuario
        }
    }

    @FXML
    void openLogin() throws IOException {
        Stage stage = (Stage) loginLink.getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource("login-view.fxml"));
        stage.setScene(new Scene(root));
    }
}
