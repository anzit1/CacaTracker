package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.utils.CompressionImagen;
import com.ct.cacatrackerproject.clases.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;


public class CrearincidenciaView {

    public TextField direccionInput;
    public ListView suggestionList;
    public TextField codigoPostalInput;
    public Button subirFotoButton;
    public ImageView imagenPreview;
    public TextField nombreArtisticoInput;
    public Button crearButton;
    private File imagenSeleccionada = null;

    @FXML
    private Button volverButton;

    @FXML
    private Text nombreUsuarioTxt;

    private final ObservableList<String> filteredAddresses = FXCollections.observableArrayList();

    public void initialize() {
        String username = UserSession.getInstance().getUsername();
        nombreUsuarioTxt.setText("Usuario: " + username);

        suggestionList.setItems(filteredAddresses);

        direccionInput.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSuggestions(newValue);
        });

        suggestionList.setOnMouseClicked(event -> {
            String selectedAddress = (String) suggestionList.getSelectionModel().getSelectedItem();
            if (selectedAddress != null) {
                String[] parts = selectedAddress.split(" \\(");
                String direccion = parts[0];
                String codigoPostal = parts[1].replace(")", "");
                direccionInput.setText(direccion);
                codigoPostalInput.setText(codigoPostal);
                suggestionList.setVisible(false);
                direccionInput.requestFocus();
            }
        });
    }

    private void updateSuggestions(String input) {
        if (input.isEmpty()) {
            suggestionList.setVisible(false);
            return;
        }
        filteredAddresses.clear();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cacatracker", "root", "1234")) {
            String query = "SELECT Direccion, CodigoPostal FROM Direcciones WHERE Direccion LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, "%" + input + "%");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String address = resultSet.getString("Direccion");
                String postalCode = resultSet.getString("CodigoPostal");
                filteredAddresses.add(address + " (" + postalCode + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        suggestionList.setVisible(!filteredAddresses.isEmpty());
        suggestionList.toFront();
    }

    public void volverAction(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/mainuserpage-view.fxml"));
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

    public void crearIncidenciaButton(ActionEvent actionEvent) throws IOException {

        String nombreArtistico = nombreArtisticoInput.getText().trim();
        String direccion = direccionInput.getText().trim();
        String codigoPostal = codigoPostalInput.getText().trim();


        if (direccion.isEmpty() || codigoPostal.isEmpty() || imagenSeleccionada == null || nombreArtistico.isEmpty()) {
            showAlert("Error en datos.", "Hay datos vacíos.", Alert.AlertType.ERROR);
            System.out.println("Hay campos vacíos.");
            return;
        }

        int userId = UserSession.getInstance().getUserId();
        if (userId == -1) {
            showAlert("Error Usuario.", "Usuario no está logeado", Alert.AlertType.ERROR);
            System.out.println("User no logeado!");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/cacatracker";
        String user = "root";
        String password = "1234";

        String insertSQL = "INSERT INTO incidencias (id_users, direccion, codigopostal, foto, nombreartistico) VALUES (?, ?, ?, ?, ?)";

        CompressionImagen compressionImagen = new CompressionImagen();
        imagenSeleccionada = compressionImagen.resizeImagen(imagenSeleccionada,1024, 1024, 10 * 1024 * 1024);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(insertSQL);
             FileInputStream fis = new FileInputStream(imagenSeleccionada)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, direccion);
            pstmt.setString(3, codigoPostal);
            pstmt.setBinaryStream(4, fis, (int) imagenSeleccionada.length());
            pstmt.setString(5, nombreArtistico);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Incidencia creada.", "Has creado una nueva incidencia", Alert.AlertType.INFORMATION);
                System.out.println("Incidencia creada!");

                Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/imagenes/SinFoto.jpg")).toExternalForm());
                imagenPreview.setImage(image);
                imagenSeleccionada = null;
                nombreArtisticoInput.clear();
                codigoPostalInput.clear();
                direccionInput.clear();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void subirFotoAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen.");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp")
        );

        File file = fileChooser.showOpenDialog(subirFotoButton.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString(), 181, 154, true, true);
            imagenPreview.setImage(image);
            imagenSeleccionada = file;
        }
    }
}