package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.utils.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResetpasswordView {
    public PasswordField passwordInput;
    public PasswordField repetirPasswordInput;
    public TextField codigoEmailInput;
    public TextField correoUserInput;
    public Button volverButton;
    public Button restablecerButton;

    // # RESET CONTRASEÑA - SETUP
    //
    public void restablecerButtonPress(ActionEvent actionEvent) {

        String password = passwordInput.getText().trim();
        String repitePassword = repetirPasswordInput.getText().trim();
        String codigoEmail = codigoEmailInput.getText().trim();
        String emailUser = correoUserInput.getText().trim();

        if (password.isEmpty() || codigoEmail.isEmpty() || emailUser.isEmpty() || repitePassword.isEmpty()) {
            showAlert("Revisa todos los campos", "Hay campos vacíos", Alert.AlertType.INFORMATION);
        } else {
            if (password.equals(repetirPasswordInput.getText())) {

                String endpoint = "/users/resetcheckfinalpassword";
                ObjectMapper objectMapper = new ObjectMapper();
                String passwordHashed = hashPassword(password);
                Map<String, String> requestData = new HashMap<>();
                requestData.put("password", passwordHashed);
                requestData.put("codigoReset", codigoEmail);
                requestData.put("email", emailUser);

                try {
                    String jsonRequest = objectMapper.writeValueAsString(requestData);
                    String response = ApiClient.sendPostRequest(endpoint, jsonRequest);

                    System.out.println(response);

                    if (response.contains("PASSRESETOK")) {
                        showAlert("Exito", "Tu contraseña ha sido restablecida.", Alert.AlertType.INFORMATION);
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/loginpage-view.fxml"));
                            AnchorPane root = fxmlLoader.load();

                            Scene scene = new Scene(root);
                            scene.getRoot().requestFocus();
                            Stage stage = (Stage) volverButton.getScene().getWindow();
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else if (response.contains("PASSRESETKO")) {
                        showAlert("Error", "Codigo de restablecimiento de contraseña errado", Alert.AlertType.ERROR);
                    } else {
                        showAlert("Error", "Error de servidor", Alert.AlertType.ERROR);
                    }
                } catch (IOException e) {
                    showAlert("Error", "No se pudo enviar la solicitud de recuperación.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            } else {
                showAlert("Error en contraseña", "Las contraseñas no coinciden.", Alert.AlertType.INFORMATION);
            }
        }
    }

    public void volverButtonPress(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/loginpage-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) volverButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // # GENERICO DE DIALOG ALERTAS
    //
    @FXML
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
