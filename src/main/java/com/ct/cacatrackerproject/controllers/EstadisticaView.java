package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.*;
import com.ct.cacatrackerproject.utils.ApiClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticaView {

    public BarChart barchart;
    public Text nombreUsuarioTxt;
    public Button volverButton;
    public BarChart<String, Number> grafico;
    public Button calleAvTodos;
    public Button codPostalTodos;
    public Button calleAvTu;
    public Button codPostalTu;
    public NumberAxis yAxis;
    public CategoryAxis xAxis;
    public Pane chartContainer;
    public AnchorPane anchorMain;
    public Pane graphContainer;
    private int userID;
    private String username;

    public void initialize() {
        userID = UserSession.getInstance().getId();
        username = UserSession.getInstance().getUsername();
        nombreUsuarioTxt.setText("Usuario: " + username);
    }

    private void renderBarChart(List<String> categorias, XYChart.Series<String, Number> series, String title) {

        graphContainer.getChildren().clear();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Categoria");
        xAxis.setCategories(FXCollections.observableArrayList(categorias));
        xAxis.setAutoRanging(false);
        xAxis.setTickLabelRotation(90);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(10);
        yAxis.setTickUnit(1);

        BarChart<String, Number> newChart = new BarChart<>(xAxis, yAxis);
        newChart.setTitle(title);
        newChart.setLegendVisible(true);
        newChart.setAnimated(true);

        newChart.setPrefSize(391, 307);
        newChart.setMinSize(391, 307);
        newChart.setMaxSize(391, 307);
        //newChart.setManaged(true);

        newChart.getData().add(series);

        graphContainer.getChildren().add(newChart);
    }

    public void codPostalTu(ActionEvent actionEvent) {

        try {
            String jsonResponse = ApiClient.sendGetStringRequest("/incidencias/top5cp/user/" + userID);

            ObjectMapper objectMapper = new ObjectMapper();

            List<CodigoPostalCountDTO> respuesta = objectMapper.readValue(jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CodigoPostalCountDTO.class));

            if (!respuesta.isEmpty()) {

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Incidencias por Codigo Postal");
                List<String> categorias = new ArrayList<>();


                for (CodigoPostalCountDTO dto : respuesta) {
                    categorias.add(dto.getCodigopostal());
                    series.getData().add(new XYChart.Data<>(dto.getCodigopostal(), dto.getTotal()));
                }

                renderBarChart(categorias, series, "Top 5 Incidencias por Codigo Postal");

            } else {
                showAlert("Informacion", "El usuario no tiene incidencias creadas.", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar las incidencias desde el servidor.", Alert.AlertType.ERROR);
        }
    }

    public void calleAvTuButton(ActionEvent actionEvent) {

        try {
            String jsonResponse = ApiClient.sendGetStringRequest("/incidencias/top5dir/user/" + userID);

            ObjectMapper objectMapper = new ObjectMapper();

            List<DireccionCountDTO> respuesta = objectMapper.readValue(jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, DireccionCountDTO.class));

            if (!respuesta.isEmpty()) {

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Incidencias por Direccion");
                List<String> categorias = new ArrayList<>();

                for (DireccionCountDTO dto : respuesta) {
                    categorias.add(dto.getDireccion());
                    series.getData().add(new XYChart.Data<>(dto.getDireccion(), dto.getTotal()));
                }

                renderBarChart(categorias, series, "Top 5 Incidencias por Direccion");

            } else {
                showAlert("Informacion", "El usuario no tiene incidencias creadas.", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar las incidencias desde el servidor.", Alert.AlertType.ERROR);
        }
    }

    public void codPostalTodosButton(ActionEvent actionEvent) {
        try {
            String jsonResponse = ApiClient.sendGetStringRequest("/incidencias/top5cp");

            ObjectMapper objectMapper = new ObjectMapper();

            List<CodigoPostalCountDTO> respuesta = objectMapper.readValue(jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, CodigoPostalCountDTO.class));

            if (!respuesta.isEmpty()) {

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Incidencias por Codigo Postal");
                List<String> categorias = new ArrayList<>();


                for (CodigoPostalCountDTO dto : respuesta) {
                    categorias.add(dto.getCodigopostal());
                    series.getData().add(new XYChart.Data<>(dto.getCodigopostal(), dto.getTotal()));
                }

                renderBarChart(categorias, series, "Top 5 Incidencias Globales por Codigo Postal");

            } else {
                showAlert("Informacion", "El usuario no tiene incidencias creadas.", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar las incidencias desde el servidor.", Alert.AlertType.ERROR);
        }

    }

    public void calleAvTodosButton(ActionEvent actionEvent) {
        try {
            String jsonResponse = ApiClient.sendGetStringRequest("/incidencias/top5dir");

            ObjectMapper objectMapper = new ObjectMapper();

            List<DireccionCountDTO> respuesta = objectMapper.readValue(jsonResponse,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, DireccionCountDTO.class));

            if (!respuesta.isEmpty()) {

                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName("Incidencias por Direccion");
                List<String> categorias = new ArrayList<>();


                for (DireccionCountDTO dto : respuesta) {
                    categorias.add(dto.getDireccion());
                    series.getData().add(new XYChart.Data<>(dto.getDireccion(), dto.getTotal()));
                }

                renderBarChart(categorias, series, "Top 5 Incidencias Globales por Direccion");

            } else {
                showAlert("Informacion", "El usuario no tiene incidencias creadas.", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar las incidencias desde el servidor.", Alert.AlertType.ERROR);
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

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
