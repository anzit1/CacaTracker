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
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VertodasincidenciasView {
    @FXML
    public Text nombreUsuarioTxt;
    @FXML
    private VBox contenedorIncidencias;

    public Button volverButton;
    private String username;
    private int userId;
    private List<Incidencias> incidenciasList;

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
                System.out.println("Usuario no tiene incidenciasList registradas: " + userId);
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
                System.out.println("No incidenciasList found: " + jsonResponse.get("ERROR").asText());
                return;
            }

            incidenciasList = objectMapper.readValue(
                    response.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Incidencias.class)
            );

            if (incidenciasList.isEmpty()) {
                System.out.println("No incidenciasList found, but it's fine.");
                return;
            }

            for (Incidencias incidencia : incidenciasList) {
                Long incidenciaId = incidencia.getId();
                System.out.println(incidenciaId);
                String direccion = incidencia.getDireccion();
                String codigoPostal = incidencia.getCodigopostal();
                String nombreArtistico = incidencia.getNombreartistico();
                Date fechaCreacion = incidencia.getFechacreacion();
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

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = (fechaCreacion != null) ? sdf.format(fechaCreacion) : "No disponible";
                Text fechaText = new Text("Fecha de creación: " + formattedDate);
                fechaText.setFont(Font.font("Arial", 12));

                textContainer.getChildren().addAll(dirText, cpText, artistText, fechaText);

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

    @FXML
    public void orderByDate(ActionEvent event) {
        if (incidenciasList != null) {
            incidenciasList = incidenciasList.stream()
                    .sorted(Comparator.comparing(Incidencias::getFechacreacion, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

            reorderIncidencias();
        }
    }

    @FXML
    public void orderByNombreArtistico(ActionEvent event) {
        if (incidenciasList != null) {
            incidenciasList = incidenciasList.stream()
                    .sorted(Comparator.comparing(Incidencias::getNombreartistico, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

            reorderIncidencias();
        }
    }

    @FXML
    public void orderByCodigoPostal(ActionEvent event) {
        incidenciasList = incidenciasList.stream()
                .sorted(Comparator.comparing(Incidencias::getCodigopostal,Comparator.nullsFirst(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        reorderIncidencias();
    }

    private void reorderIncidencias() {

        contenedorIncidencias.getChildren().clear();

        for (Incidencias incidencia : incidenciasList) {
            HBox itemContainer = createItemContainer(incidencia);
            contenedorIncidencias.getChildren().add(itemContainer);
        }
    }

    private HBox createItemContainer(Incidencias incidencia) {
        Long incidenciaId = incidencia.getId();
        String direccion = incidencia.getDireccion();
        String codigoPostal = incidencia.getCodigopostal();
        String nombreArtistico = incidencia.getNombreartistico();
        Date fechaCreacion = incidencia.getFechacreacion();
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

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = (fechaCreacion != null) ? sdf.format(fechaCreacion) : "No disponible";
        Text fechaText = new Text("Fecha de creación: " + formattedDate);
        fechaText.setFont(Font.font("Arial", 12));

        textContainer.getChildren().addAll(dirText, cpText, artistText, fechaText);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);       

        itemContainer.getChildren().addAll(imageView, textContainer, spacer);
        return itemContainer;
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
