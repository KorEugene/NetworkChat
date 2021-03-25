package ru.online.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoSceneController {

    @FXML
    private Label labelMessage;
    @FXML
    private Button btnOK;

    @FXML
    private void btnOK(ActionEvent actionEvent) {
        Stage window = (Stage) btnOK.getScene().getWindow();
        window.close();
    }

    public Label getLabelMessage() {
        return labelMessage;
    }
}
