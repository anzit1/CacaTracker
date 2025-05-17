package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.utils.ApiClient;
import com.ct.cacatrackerproject.utils.CheckCodigoPostal;
import com.ct.cacatrackerproject.utils.GeneradorCodigo;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.HashMap;
import java.util.Map;


public class RegistrarpageView {

    @FXML
    private TextField nuevoCorreoInput;

    @FXML
    private Button volverButton;

    @FXML
    private Button registrarButton;

    @FXML
    private TextField nuevoUserInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private PasswordField repetirPasswordInput;

    @FXML
    private TextField codigoPostalInput;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    private boolean checkCodigoPostal(String codigoPostal) {
        CheckCodigoPostal check = new CheckCodigoPostal();
        return check.cpValido(codigoPostal);
    }

    @FXML
    public void registrarButtonPress(ActionEvent actionEvent) throws Exception {

        String userName = nuevoUserInput.getText().trim();
        String email = nuevoCorreoInput.getText().trim();
        String password = passwordInput.getText().trim();
        String passwordRepetir = repetirPasswordInput.getText().trim();
        String codigoPostal = codigoPostalInput.getText().trim();

        // # CHECK INPUTS CORRECTOS
        //
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepetir.isEmpty() || codigoPostal.isEmpty()) {
            showAlert("Datos vacíos.", "Faltan campos por completar", "Por favor, complete todos los campos.");
            return;
        }

        if (userName.length() < 4) {
            showAlert("Error en Nombre de Usuario.", "Nombre de usuario demasiado corto", "Debe tener al menos 4 caracteres.");
            return;
        }

        if (!email.matches(EMAIL_REGEX)) {
            showAlert("Error en Correo Electrónico.", "Formato de correo inválido", "Por favor, ingrese un correo válido.");
            return;
        }

        if (password.length() < 8) {
            showAlert("Error en Contraseña.", "Contraseña demasiado corta", "Debe tener al menos 8 caracteres.");
            return;
        }

        if (!password.equals(passwordRepetir)) {
            showAlert("Error en Contraseña.", "Las contraseñas no coinciden", "Por favor, verifica las contraseñas ingresadas.");
            return;
        }

        boolean bienCP = checkCodigoPostal(codigoPostal);
        if (!bienCP) {
            showAlert("Error en Código Postal.", "Código Postal incorrecto", "Ingrese un código válido de Alicante.");
            return;
        }

        // # UTILS DE DATOS
        //
        String passwordHashed = hashPassword(password);
        GeneradorCodigo gen = new GeneradorCodigo();
        String codigoActiva = gen.generaCodigo();

        // # MAPPING DE DATOS
        //
        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", userName);
        requestData.put("email", email);
        requestData.put("password", passwordHashed);
        requestData.put("codigopostal", codigoPostal);
        requestData.put("codigoactiva", codigoActiva);
        requestData.put("activado", "false");

        // # ENVIO API REQUEST
        //
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestData);
        String response = ApiClient.sendPostRequest("/users/registrar", jsonBody);

        System.out.println(response);
        if (response != null) {
            if (response.contains("OKREGISTRO")) {
                clearInput();
                showAlert("Registro exitoso", "Usuario registrado correctamente", "Por favor, revise su correo para activar la cuenta.");
            } else if (response.contains("KOUSEREMAIL")) {
                showAlert("Error en Registro", "El usuario o correo electrónico ya está registrado.", "Por favor, use otro correo o nombre de usuario.");
            } else if (response.contains("KOSERVER")) {
                showAlert("Error en Registro", "Hubo un error inesperado en el servidor", "Por favor, intente nuevamente.");
            }
        } else {
            showAlert("Error en Registro", "No se pudo conectar con el servidor", "Por favor, revise su conexión a internet.");
        }
    }

    @FXML
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

    @FXML
    public void limpiarButtonPress(ActionEvent actionEvent) {

        nuevoUserInput.clear();
        passwordInput.clear();
        repetirPasswordInput.clear();
        nuevoCorreoInput.clear();
        codigoPostalInput.clear();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void clearInput() {
        nuevoUserInput.clear();
        passwordInput.clear();
        repetirPasswordInput.clear();
        nuevoCorreoInput.clear();
        codigoPostalInput.clear();
    }

}




