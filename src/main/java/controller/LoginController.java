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

public class LoginController {

    @FXML
    private TextField user;

    @FXML
    private TextField password;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    private Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void login(ActionEvent event) {
        String userName = user.getText();
        String pass = password.getText();
        if (userName == null || userName == "" || pass == null || pass == "" || userName.length() == 0 || pass.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "You need to input username and password");
            alert.show();
            return;
        }
        pass = AES.encrypt(pass, "stockapp");
        try {
            if (UserDAO.isLoginInformationCorrect(userName, pass)) {

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong username or password");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void register(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RegisterPanel.fxml"));
        Parent root = loader.load();
        RegisterController controller = loader.getController();
        System.out.println(primaryStage == null);
        controller.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Register");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
