package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.Direccion;
import com.ct.cacatrackerproject.utils.ApiClient;
import com.ct.cacatrackerproject.utils.CompressionImagen;
import com.ct.cacatrackerproject.clases.UserSession;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.*;


public class CrearincidenciaView {

    public TextField direccionInput;
    public ListView suggestionList;
    public TextField codigoPostalInput;
    public Button subirFotoButton;
    public ImageView imagenPreview;
    public TextField nombreArtisticoInput;
    public Button crearButton;
    private File imagenSeleccionada = null;
    private ArrayList<Direccion> listaDeCalles;

    @FXML
    private Button volverButton;

    @FXML
    private Text nombreUsuarioTxt;

    private final ObservableList<String> filteredAddresses = FXCollections.observableArrayList();

    public void initialize() {
        listaDeCalles = new ArrayList<Direccion>();
        String username = UserSession.getInstance().getUsername();
        nombreUsuarioTxt.setText("Usuario: " + username);
        listaDeCalles.clear();
        cargaTodasDirecciones();
        suggestionList.setItems(filteredAddresses);

        //  Actualiza lista segun escrita del usuario
        direccionInput.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSuggestions(newValue);
        });

        // Selecciona la direccion de la lista de direcciones
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

    // # CARGA EN MEMORIA LA LISTA DE TODAS LAS DIRECCIONES
    //
    private void cargaTodasDirecciones() {
        try {
            ArrayList<Direccion> direcciones = ApiClient.sendGetRequest("/direcciones", Direccion.class);
            filteredAddresses.clear();
            listaDeCalles.addAll(direcciones);
            System.out.println(listaDeCalles.get(1));

        } catch (IOException e) {
            System.err.println("Error cargando direcciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // # FILTRA LISTA DE DIRECCIONES MIENTRAS USUARIO ESCRIBE
    //
    private void updateSuggestions(String input) {
        if (input.isEmpty()) {
            suggestionList.setVisible(false);
            return;
        }
        filteredAddresses.clear();
        String lowerCaseInput = input.toLowerCase();
        for (Direccion direccion : listaDeCalles) {
            if (direccion.getDireccion().toLowerCase().contains(lowerCaseInput)) {
                filteredAddresses.add(direccion.getDireccion() + " (" + direccion.getCodigoPostal() + ")");
            }
        }

        suggestionList.setVisible(!filteredAddresses.isEmpty());
        suggestionList.toFront();
    }

    public void crearIncidenciaButton(ActionEvent actionEvent) throws IOException {

        String userName = UserSession.getInstance().getUsername();
        String nombreArtistico = nombreArtisticoInput.getText().trim();
        String direccion = direccionInput.getText().trim();
        String codigoPostal = codigoPostalInput.getText().trim();
        CompressionImagen compressionImagen = new CompressionImagen();
        imagenSeleccionada = compressionImagen.resizeImagen(imagenSeleccionada, 1024, 1024, 10 * 1024 * 1024); // 10 MB size limit
        String imagenCodificada64 = CompressionImagen.encodeImageToBase64(imagenSeleccionada);

        if (direccion.isEmpty() || codigoPostal.isEmpty() || imagenSeleccionada == null || nombreArtistico.isEmpty()) {
            showAlert("Error en datos.", "Hay datos vacíos.", Alert.AlertType.ERROR);
            System.out.println("Hay campos vacíos.");
            return;
        }
        Map<String, String> requestData = new HashMap<>();
        requestData.put("nombreArtistico",nombreArtistico);
        requestData.put("direccion",direccion);
        requestData.put("codigoPostal",codigoPostal);
        requestData.put("imagenCodificada64",imagenCodificada64);
        requestData.put("username",userName);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(requestData);
        String response = ApiClient.sendPostRequest("/incidencias/nuevaincidencia", jsonBody);

        System.out.println(response);

        if (response != null) {
            if (response.contains("OKINCIDENCIA")) {
                clearInput();
                showAlert("Incidencia creada", "Gracias por crear una nueva incidencia.", Alert.AlertType.INFORMATION);
            } else if (response.contains("KOINCIDENCIA")) {
                showAlert("Error en creacion de incidencia", "Algo malo ha pasado, la incidencia no ha sido cread.", Alert.AlertType.ERROR);
            } else if (response.contains("KOSERVER")) {
                showAlert("Error en servidor", "Hubo un error inesperado en el servidor", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Error en servidor", "No se pudo conectar con el servidor", Alert.AlertType.ERROR);
        }
    }

    private void clearInput() {
        nombreArtisticoInput.clear();
        direccionInput.clear();
        codigoPostalInput.clear();
        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/imagenes/SinFoto.jpg")).toExternalForm());
        imagenPreview.setImage(image);
        imagenSeleccionada = null;
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