package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;


public class VertusincidenciasView {

    @FXML
    public Text nombreUsuarioTxt;

    @FXML
    public Button volverButton;

    @FXML
    private VBox contenedorIncidencias;

    private String username;
    private int userId;
    private String userIdString;
    private final String url = "jdbc:mysql://localhost:3306/cacatracker";
    private final String user = "root";
    private final String password = "1234";

    public void initialize(){
        username = UserSession.getInstance().getUsername();
        userId = UserSession.getInstance().getUserId();
        userIdString = Integer.toString(userId);
        nombreUsuarioTxt.setText(username);
        cargarIncidencias();
    }

    private void cargarIncidencias() {
        contenedorIncidencias.getChildren().clear();

        String query = "SELECT id, direccion, codigopostal, foto, nombreartistico FROM incidencias WHERE id_users = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int incidenciaId = resultSet.getInt("id");
                String direccion = resultSet.getString("direccion");
                String codigoPostal = resultSet.getString("codigopostal");
                String nombreArtistico = resultSet.getString("nombreartistico");
                byte[] fotoBytes = resultSet.getBytes("foto");


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

                Button verButton = new Button("Editar");
                Button borrarButton = new Button("Borrar");
                verButton.setOnAction(event -> openEditDialog(incidenciaId,nombreArtistico, direccion, codigoPostal));
                borrarButton.setOnAction(event -> confirmDelete(incidenciaId));


                itemContainer.getChildren().addAll(imageView, textContainer, spacer, verButton,borrarButton);
                contenedorIncidencias.getChildren().add(itemContainer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void openEditDialog(int incidenciaId,String nombreArtistico, String direccion, String codigoPostal) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Editar Incidencia");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(15));

        TextField nombreArtField = new TextField(nombreArtistico);
        TextField direccionField = new TextField(direccion);
        TextField codigoPostalField = new TextField(codigoPostal);

        Button saveButton = new Button("Guardar Cambios");

        saveButton.setOnAction(e -> {
            String nuevoNombreArt = nombreArtField.getText().trim();
            String nuevaDireccion = direccionField.getText().trim();
            String nuevoCodigoPostal = codigoPostalField.getText().trim();

            if (nuevoNombreArt.isEmpty() || nuevaDireccion.isEmpty() || nuevoCodigoPostal.isEmpty()) {
                showAlert("Error", "Todos los campos deben estar completos.", Alert.AlertType.ERROR);
                return;
            }

            updateIncidenciaInDB(incidenciaId, nuevoNombreArt, nuevaDireccion, nuevoCodigoPostal);
            cargarIncidencias();
            dialogStage.close();
        });

        vbox.getChildren().addAll(
                new Label("Artista:"), nombreArtField,
                new Label("Dirección:"), direccionField,
                new Label("Código Postal:"), codigoPostalField,
                saveButton
        );

        Scene scene = new Scene(vbox, 300, 250);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    private void updateIncidenciaInDB(int incidenciaId,String nuevoNombreArt, String nuevaDireccion, String nuevoCodigoPostal) {
        String sql = "UPDATE incidencias SET nombreartistico=?, direccion=?, codigopostal=? WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoNombreArt);
            pstmt.setString(2, nuevaDireccion);
            pstmt.setString(3, nuevoCodigoPostal);
            pstmt.setInt(4, incidenciaId);
            pstmt.executeUpdate();

            System.out.println("Incidencia actualizada.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void confirmDelete(int incidenciaId) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Eliminación");
        alert.setHeaderText("Seguro que deseas borrar esta incidencia?");
        alert.setContentText("Esta acción no se puede deshacer.");

        ButtonType yesButton = new ButtonType("Sí", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                deleteIncidenciaFromDB(incidenciaId);
                cargarIncidencias();
            }
        });
    }
    private void deleteIncidenciaFromDB(int incidenciaId) {
        String sql = "DELETE FROM incidencias WHERE id=?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, String.valueOf(incidenciaId));
            pstmt.executeUpdate();
            System.out.println("Incidencia eliminada.");
        } catch (SQLException e) {
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
