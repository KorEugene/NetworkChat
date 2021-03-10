package ru.online.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AlertSceneController {

    @FXML
    private Button btnOK;

    @FXML
    private void btnOK(ActionEvent actionEvent) {
        Stage window = (Stage) btnOK.getScene().getWindow();
        window.close();
    }
}
