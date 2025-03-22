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
            Stage stage = (Stage) logoutButton.getScene().getWindow();
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
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void editaIncidencia(ActionEvent actionEvent) {
    }

    public void verTodasIncidencias(ActionEvent actionEvent) {
    }

    public void entraCreaInc(MouseEvent mouseEvent) {
        explicaButton.setText("Explica Cria incidencia");
    }

    public void saiCreaInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void entraTodasInc(MouseEvent mouseEvent) {
        explicaButton.setText("Explica Todas incidencia");
    }

    public void saiTodasInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void entraEditaInc(MouseEvent mouseEvent) {
        explicaButton.setText("Explica Edita incidencia");
    }

    public void saiEditaInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void entraTusInc(MouseEvent mouseEvent) {
        explicaButton.setText("Explica Tus incidencia");
    }

    public void saiTusInc(MouseEvent mouseEvent) {
        limpiaExplica();
    }

    public void limpiaExplica(){
        explicaButton.setText("");
    }

    public void verEstadisticas(ActionEvent actionEvent) {
    }
}
