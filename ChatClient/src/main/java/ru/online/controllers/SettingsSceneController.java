package ru.online.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsSceneController {

    @FXML
    private TextField labIPAddress;
    @FXML
    private TextField labIncomingPort;
    @FXML
    private TextField labOutgoingPort;
    @FXML
    private Button btnApplySettings;
    @FXML
    private Button btnCancelSettings;

    @FXML
    private void applyButtonAction(ActionEvent actionEvent) {
        //Do something for apply settings
    }

    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage window = (Stage) btnCancelSettings.getScene().getWindow();
        window.close();
    }
}
