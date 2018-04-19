package controller;

import dao.UserDAO;
import encryption.AES;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField userNameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Button submitButton;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void submit(ActionEvent event) {
        String user = userNameTextField.getText();
        String pass = passwordTextField.getText();
        String email = emailTextField.getText();

        if (user == "" || user == null || pass == "" || pass == null || email == "" || email == null
                || user.length() == 0 || pass.length() == 0 || email.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You need to input username, password and email");
            alert.show();
            return;
        }

        pass = AES.encrypt(pass, "stockapp");

        try {
            if (UserDAO.isUserWithNameOrEmailExists(user, email)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Username or email already exist");
                alert.show();
                return;
            } else {
                UserDAO.addUser(user, pass, email);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Register success, please login");
                alert.show();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginPanel.fxml"));
                Parent root = null;
                LoginController controller = null;
                try {
                    root = loader.load();
                    controller = (LoginController) loader.getController();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                controller.setPrimaryStage(primaryStage);
                primaryStage.setTitle("Login");
                primaryStage.setScene(new Scene(root));
                primaryStage.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
