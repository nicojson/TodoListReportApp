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
import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDao;
import tecnm.celaya.edu.mx.todolistreportapp.dao.UsuarioDaoImpl;
import tecnm.celaya.edu.mx.todolistreportapp.model.LoginModel;
import tecnm.celaya.edu.mx.todolistreportapp.model.Usuario;

import java.io.IOException;

public class LoginController {

    @FXML private StackPane rootPane;
    @FXML private JFXTextField usernameField;
    @FXML private JFXPasswordField passwordField;
    @FXML private Label signUpLabel;

    private LoginModel loginModel;

    public void initialize() {
        this.loginModel = new LoginModel();
    }

    @FXML
    void login() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMaterialDialog("Campos Vacíos", "Por favor, ingrese su nombre de usuario y contraseña.");
            return;
        }

        boolean isAuthenticated = loginModel.authenticate(username, password);

        if (isAuthenticated) {
            // Obtener la ventana actual (la de login) para cerrarla.
            Stage loginStage = (Stage) rootPane.getScene().getWindow();
            loginStage.close();

            // Cargar la vista principal en una nueva ventana.
            UsuarioDao usuarioDao = new UsuarioDaoImpl();
            Usuario currentUser = usuarioDao.findByUsername(username).orElse(null);

            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.initData(currentUser);

            Stage mainStage = new Stage();
            mainStage.setTitle("Mis Tareas - " + currentUser.getNombreUsuario());
            mainStage.setScene(new Scene(root));
            mainStage.setMaximized(true); // Maximizar la nueva ventana.
            mainStage.show();

        } else {
            showMaterialDialog("Error de Autenticación", "Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    void openRegister() throws IOException {
        Stage stage = (Stage) signUpLabel.getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource("register-view.fxml"));
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
