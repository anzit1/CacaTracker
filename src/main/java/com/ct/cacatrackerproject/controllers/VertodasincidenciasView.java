package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.Incidencias;
import com.ct.cacatrackerproject.clases.UserSession;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class VertodasincidenciasView {
    @FXML
    public Text nombreUsuarioTxt;
    @FXML
    private VBox contenedorIncidencias;

    public Button volverButton;
    private String username;
    private int userId;

    public void initialize(){
        username = UserSession.getInstance().getUsername();
        userId = UserSession.getInstance().getId();
        nombreUsuarioTxt.setText("Usuario: " + username);
        cargarIncidencias();
    }

    private void cargarIncidencias() {

        contenedorIncidencias.getChildren().clear();
        String apiUrl = "http://localhost:8080/cacatrackerapi/rest/incidencias";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.connect();

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) { // 404
                System.out.println("Usuario no tiene incidencias registradas: " + userId);
                return;
            }

            if (responseCode != HttpURLConnection.HTTP_OK) {
                System.out.println("Server responded with error code: " + responseCode);
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());

            if (jsonResponse.has("ERROR")) {
                System.out.println("No incidencias found: " + jsonResponse.get("ERROR").asText());
                return;
            }

            List<Incidencias> incidencias = objectMapper.readValue(
                    response.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Incidencias.class)
            );

            if (incidencias.isEmpty()) {
                System.out.println("No incidencias found, but it's fine.");
                return;
            }

            for (Incidencias incidencia : incidencias) {
                Long incidenciaId = incidencia.getId();
                System.out.println(incidenciaId);
                String direccion = incidencia.getDireccion();
                String codigoPostal = incidencia.getCodigopostal();
                String nombreArtistico = incidencia.getNombreartistico();
                byte[] fotoBytes = incidencia.getFoto();

                HBox itemContainer = new HBox(10);
                itemContainer.setStyle("-fx-border-color: #ddd; -fx-padding: 10px; -fx-background-color: #f9f9f9; -fx-border-radius: 5px;");
                itemContainer.setAlignment(Pos.CENTER_LEFT);
                itemContainer.setMaxWidth(Double.MAX_VALUE);

                ImageView imageView = new ImageView();
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                if (fotoBytes != null) {
                    Image img = new Image(new ByteArrayInputStream(fotoBytes));
                    imageView.setImage(img);
                } else {
                    imageView.setImage(new Image(getClass().getResource("/com/imagenes/SinFoto.jpg").toExternalForm()));
                }

                VBox textContainer = new VBox(5);

                Text dirText = new Text("Dirección: " + direccion);
                dirText.setFont(Font.font("Arial", 14));

                Text cpText = new Text("Código Postal: " + codigoPostal);
                cpText.setFont(Font.font("Arial", 12));

                Text artistText = new Text("Nombre artístico: " + nombreArtistico);
                artistText.setFont(Font.font("Arial Black", 16));

                textContainer.getChildren().addAll(dirText, cpText, artistText);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                itemContainer.getChildren().addAll(imageView, textContainer, spacer);
                contenedorIncidencias.getChildren().add(itemContainer);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar las incidencias desde el servidor.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
}
