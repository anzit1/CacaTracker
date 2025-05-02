package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.MainApplication;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginpageView {

    public Button olvidePassButton;
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
        System.out.println("7RESPUESTA SERVIDOR LOGINBUTTONPRESS METHOD");
        System.out.println(response);

        if (response != null) {
            JsonNode jsonResponseNode = objMap.readTree(response);
            if (jsonResponseNode.has("EXITO")) {
                String message = jsonResponseNode.get("EXITO").asText();
                System.out.println(message);
                if (message.contains("OKLOGIN")) {

                    UserSession.getInstance();
                    String token = jsonResponseNode.get("token").asText();
                    String idUser = jsonResponseNode.get("idUser").asText();
                    UserSession.getInstance().setUsername(username);
                    UserSession.getInstance().setToken(token);
                    UserSession.getInstance().setId(Integer.valueOf(idUser));

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/mainuserpage-view.fxml"));
                    Parent root = fxmlLoader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            } else if (jsonResponseNode.has("ERROR")) {
                String errorMessage = jsonResponseNode.get("ERROR").asText();
                System.out.println(errorMessage);
                if (errorMessage.contains("KOLOGIN")) {
                    showAlert("Error en Login", "Contraseña o usuario incorrecto.", Alert.AlertType.ERROR);
                }
            } else if (jsonResponseNode.has("ACTIVA")) {
                String activaMessage = jsonResponseNode.get("ACTIVA").asText();
                System.out.println(activaMessage);
                if (activaMessage.contains("KOLOGINACTIVA")) {
                    showAlert("Cuenta no activada", "Tu cuenta no está activada, mira en tu correo el código y activala.", Alert.AlertType.INFORMATION);
                    activarCuentaDialogo(username);
                }
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


    // # RESET CONTRASEÑA - SETUP
    //
    public void olvidePassPress(ActionEvent actionEvent) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/resetpassword-view.fxml"));
            AnchorPane root = fxmlLoader.load();

             Scene scene = new Scene(root);

            scene.getRoot().requestFocus();
            Stage stage = (Stage) olvidePassButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Recuperar Contraseña");
        dialog.setHeaderText("Ingrese su email para restablecer la contraseña");

        TextField emailInput = new TextField();
        emailInput.setPromptText("Email");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        dialog.getDialogPane().setContent(emailInput);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return emailInput.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(email -> {
            if (email.contains("@")) {
                sendPasswordResetRequest(email);
                System.out.println(" ESTE _> " +email);
            } else {
                showAlert("Error", "Ingrese un email válido.", Alert.AlertType.ERROR);
            }
        });
    }

    // # RESET CONTRASEÑA - POST
    //
    private void sendPasswordResetRequest(String email) {

        String endpoint = "/users/resetpassword";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestData = new HashMap<>();
        requestData.put("email", email);

        try {
            String jsonRequest = objectMapper.writeValueAsString(requestData);
            ApiClient.sendPostRequest(endpoint, jsonRequest);
            showAlert("Recuperación de Contraseña",
                    "Revise su correo electrónico, tendrá un código para continuar con el restablecimiento de contraseña.",
                    Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            showAlert("Error", "No se pudo enviar la solicitud de recuperación.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    // # ACTIVAR CUENTA NUEVA - SETUP
    //
    private void activarCuentaDialogo(String username) {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Activa tu cuenta");
        dialog.setHeaderText("Ingrese el codigo de activacion que está en tu correo.");

        TextField codigoInput = new TextField();
        codigoInput.setPromptText("Codigo");

        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

        dialog.getDialogPane().setContent(codigoInput);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return codigoInput.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(codigo -> sendCodigoActivaRequest(codigo, username));

    }

    // # ACTIVAR CUENTA NUEVA - POST
    //
    private void sendCodigoActivaRequest(String codigo, String username) {

        String endpoint = "/users/activacuenta";
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", username);
        requestData.put("codigoactiva", codigo);

        try {
            String jsonRequest = objectMapper.writeValueAsString(requestData);
            String response = ApiClient.sendPostRequest(endpoint, jsonRequest);

            System.out.println(response);
            if (response.contains("USUARIOACTIVADO")) {
                showAlert("Exito", "Cuenta activada! Vuelve a escribir tus credenciales para entrar.", Alert.AlertType.CONFIRMATION);
            } else if (response.contains("BADCODEACTIVA")) {
                showAlert("Error", "Codigo de activacion incorrecto", Alert.AlertType.ERROR);
            } else if (response.contains("BADUSERNOTFOUND")) {
                showAlert("Error", "Usuario no existe", Alert.AlertType.ERROR);
            } else {
                showAlert("Error", "Error de servidor", Alert.AlertType.ERROR);
            }
        } catch (IOException e) {
            showAlert("Error", "No se pudo enviar la solicitud de recuperación.", Alert.AlertType.ERROR);
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
}