package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.utils.CheckCodigoPostal;
import com.ct.cacatrackerproject.utils.EmailConfirmarCuenta;
import com.ct.cacatrackerproject.utils.GeneradorCodigo;
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

    // Encryptar la contraseña
    private String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    // Verifica codigo postal
    private boolean checkCodigoPosta(String codigoPostal) {
        CheckCodigoPostal check = new CheckCodigoPostal();
        return check.cpValido(codigoPostal);
    }

    @FXML
    public void registrarButtonPress(ActionEvent actionEvent) throws Exception {

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cacatracker", "root", "1234");

        if (!nuevoUserInput.getText().isEmpty() &&
                !passwordInput.getText().isEmpty() &&
                !repetirPasswordInput.getText().isEmpty() &&
                !codigoPostalInput.getText().isEmpty()) {

            String userName = nuevoUserInput.getText();
            String email = nuevoCorreoInput.getText();

            String checkUserQuery = "SELECT COUNT(*) FROM USERS WHERE username = ? OR email = ?";
            PreparedStatement checkUserStmt = connection.prepareStatement(checkUserQuery);
            checkUserStmt.setString(1, userName);
            checkUserStmt.setString(2, email);
            ResultSet rs = checkUserStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) > 0) {
                showAlert("Error en registro de usuario.", "Usuario o correo ya en uso", "Por favor, elige otro nombre de usuario o correo.");
            } else {
                String password = passwordInput.getText();
                String passwordRepetir = repetirPasswordInput.getText();

                if (!password.equals(passwordRepetir)) {
                    showAlert("Error en contraseña.", "Las contraseñas no coinciden", "Por favor, verifica las contraseñas ingresadas.");
                } else {
                    String passwordHashed = hashPassword(password);

                    String codigoPostal = codigoPostalInput.getText();
                    boolean bienCP = checkCodigoPosta(codigoPostal);
                    if (!bienCP) {
                        showAlert("Error en Código Postal.", "Código Postal incorrecto", "Ingrese un código válido de Alicante.");
                    } else {
                        GeneradorCodigo gen = new GeneradorCodigo();
                        String codigoActiva = gen.generaCodigo();
                        String insertQuery = "INSERT INTO USERS (username, email, password, codigopostal, codigoactiva, activado) VALUES (?, ?, ?, ?, ?, ?)";

                        PreparedStatement insertStmt = connection.prepareStatement(insertQuery);
                        insertStmt.setString(1, userName);
                        insertStmt.setString(2, email);
                        insertStmt.setString(3, passwordHashed);
                        insertStmt.setString(4, codigoPostal);
                        insertStmt.setString(5, codigoActiva);
                        insertStmt.setBoolean(6, false);
                        int rowsInserted = insertStmt.executeUpdate();

                        if (rowsInserted > 0) {
                            EmailConfirmarCuenta emailEnviado = new EmailConfirmarCuenta();
                            emailEnviado.emailConfirmadorCuenta(email, codigoActiva);
                            System.out.println("User registered successfully!");
                        } else {
                            System.out.println("Error registering user.");
                        }
                    }
                }
            }
        } else {
            showAlert("Datos vacíos.", "Faltan campos por completar", "Por favor, complete todos los campos.");
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
        codigoPostalInput.clear();
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


