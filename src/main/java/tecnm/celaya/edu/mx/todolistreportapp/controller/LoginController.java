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
import tecnm.celaya.edu.mx.todolistreportapp.model.LoginModel;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;
import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDaoImpl;
import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDao;

import java.io.IOException;

public class LoginController {

    @FXML
    private JFXTextField usernameField;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private Label signUpLabel;

    private LoginModel loginModel;

    public void initialize() {
        this.loginModel = new LoginModel();
    }

    @FXML
    void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("Por favor, ingrese usuario y contraseña.");
            return;
        }

        boolean isAuthenticated = loginModel.authenticate(username, password);

        if (isAuthenticated) {
            System.out.println("¡Inicio de sesión exitoso!");
            
            // Obtener los datos del usuario para pasarlos a la siguiente pantalla
            UsuarioDao usuarioDao = new UsuarioDaoImpl();
            Usuario currentUser = usuarioDao.findByUsername(username).orElse(null);

            // Cargar la vista principal
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
            Parent root = loader.load();

            // Pasar los datos del usuario al controlador de la vista principal
            MainController mainController = loader.getController();
            mainController.initData(currentUser);

            // Mostrar la nueva escena
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));

        } else {
            System.out.println("Error: Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    void openRegister() throws IOException {
        Stage stage = (Stage) signUpLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource("register-view.fxml"));
        stage.setScene(new Scene(root));
    }
}
