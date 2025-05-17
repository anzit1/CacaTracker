package com.ct.cacatrackerproject.controllers;

import com.ct.cacatrackerproject.clases.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


public class MainuserpageView {

    public Button crearIncidenciaButton;
    public Button estadisticasButton;
    public Button tusIncidenciaButton;
    public Button todasIncidenciaButton;

    @FXML
    private Text nombreUsuarioTxt;

    @FXML
    private Button logoutButton;

    @FXML
    private TextArea explicaButton;

    public void initialize(){
        String username = UserSession.getInstance().getUsername();
        nombreUsuarioTxt.setText("Usuario: " + username);
    }

    public void logoutAction(ActionEvent actionEvent) {

        try {
            UserSession.getInstance().clearUserInfo();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/loginpage-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void creaIncidencia(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/crearincidencia-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) crearIncidenciaButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verTusIncidencias(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/vertusincidencias-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) tusIncidenciaButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verEstadisticas(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/estadistica-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) estadisticasButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void verTodasIncidencias(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ct/cacatrackerproject/vertodasincidencias-view.fxml"));
            AnchorPane root = fxmlLoader.load();

            Scene scene = new Scene(root);
            scene.getRoot().requestFocus();
            Stage stage = (Stage) todasIncidenciaButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void entraCreaInc(MouseEvent mouseEvent) {
        explicaButton.setText(  """
                Aquí puedes crear una nueva incidencia.
                
                - Necessitas aportar:
                    Un nombre artístico que eligas
                    Selecciona la direccion de la incidencia
                    Añade una foto cómo prueba
                """);
    }

    public void saiCreaInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void entraTodasInc(MouseEvent mouseEvent) {
        explicaButton.setText(  """
                Aquí puedes consultar todas las
                incidencias globales.
                
                - Si quieres borrar alguna incidencia,
                consulta "Tus incidencias".
                """);
    }

    public void saiTodasInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void entraTusInc(MouseEvent mouseEvent) {
        explicaButton.setText(  """
                Aquí puedes consultar y borrar tus incidencias.
                """);
    }

    public void saiTusInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void entraEstadInc(MouseEvent mouseEvent) {
        explicaButton.setText(
                """
                Aquí puedes consultar las estadisticas globales 
                y personales de las incidencias.
                
                - Vista por:
                    Código Postal
                    Calle/Avenida
                """
                );
    }

    public void saiEstadInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void limpiaExplica(){
        explicaButton.clear();
    }
}
