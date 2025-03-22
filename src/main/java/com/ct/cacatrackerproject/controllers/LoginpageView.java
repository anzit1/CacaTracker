package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.*;

public class LoginpageView {

    @FXML
    private Button loginButton;

    @FXML
    private TextField userInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    public void loginButtonPress(ActionEvent actionEvent) throws SQLException {

        /*
        String checkLoginQuery = "SELECT password, activado FROM USERS WHERE email = ?";
PreparedStatement loginStmt = connection.prepareStatement(checkLoginQuery);
loginStmt.setString(1, email);
ResultSet rs = loginStmt.executeQuery();

if (rs.next()) {
    boolean isActive = rs.getBoolean("activado");
    if (!isActive) {
        showAlert("Cuenta no activada", "Debes activar tu cuenta antes de iniciar sesión.");
        return;
    }

    String hashedPassword = rs.getString("password");
    if (BCrypt.checkpw(password, hashedPassword)) {
        System.out.println("Login exitoso");
    } else {
        showAlert("Error", "Contraseña incorrecta.");
    }
} else {
    showAlert("Error", "Usuario no encontrado.");
}
        */

        String username = userInput.getText().toString();
        String password = passwordInput.getText().toString();

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cacatracker", "root", "1234");
        String query = "SELECT id, password FROM users WHERE username = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {

            String storedHashedPassword = resultSet.getString("password");
            int userId = resultSet.getInt("id");

            if (BCrypt.checkpw(password, storedHashedPassword)) {

                UserSession.getInstance().setUserInfo(username, userId);
                UserSession.getInstance().setUserInfo(username, userId);

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/mainuserpage-view.fxml"));
                    AnchorPane root = fxmlLoader.load();

                    Scene scene = new Scene(root);
                    scene.getRoot().requestFocus();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                showAlert("Login Incorrecto", "Tus datos no están correctos.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Login Incorrecto", "Usuario no existe.", Alert.AlertType.ERROR);
        }
        connection.close();
    }

    @FXML
    public void registrarButtonPress(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/registrarpage-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void salirButtonPress(ActionEvent actionEvent) {
        if (UserSession.getInstance() != null) {
            UserSession.getInstance().clearUserInfo();
        }
        Platform.exit();
    }

    @FXML
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
