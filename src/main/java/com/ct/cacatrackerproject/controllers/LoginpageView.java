package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.UserSession;
import com.ct.cacatrackerproject.utils.ApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginpageView {

    @FXML
    private Button loginButton;

    @FXML
    private TextField userInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    public void loginButtonPress(ActionEvent actionEvent) throws IOException {

        // # Setup ENDPOINT REQUEST
        //
        String endpoint = "/users/login";

        String username = userInput.getText().trim();
        String password = passwordInput.getText().trim();

        ObjectMapper objMap = new ObjectMapper();
        Map<String, String> dataJSON = new HashMap<>();

        dataJSON.put("username", username);
        dataJSON.put("password", password);
        String jsonResponse = objMap.writeValueAsString(dataJSON);

        // # Respuesta API
        //
        String response = ApiClient.sendPostRequest(endpoint, jsonResponse);
        System.out.println(response);

        if (response != null) {
            if (response.contains("OKLOGIN")) {
                UserSession.getInstance();
                JsonNode jsonResponseNode = objMap.readTree(response);

                String token = jsonResponseNode.get("token").asText();
                String usernameFromResponse = jsonResponseNode.get("username").asText();

                UserSession.getInstance().setUsername(usernameFromResponse);
                UserSession.getInstance().setToken(token);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/mainuserpage-view.fxml"));
                Parent root = fxmlLoader.load();
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } else if (response.contains("KOLOGIN")) {
                showAlert("Error en Login", "Usuario o contraseña errónea.", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error Servidor", "No se pudo conectar con el servidor", Alert.AlertType.ERROR);
        }
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