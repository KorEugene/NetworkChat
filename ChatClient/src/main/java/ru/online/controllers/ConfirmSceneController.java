package ru.online.controllers;

import ru.online.ConfirmWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmSceneController {

    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    @FXML
    private void btnPressedYes(ActionEvent actionEvent) {
        ConfirmWindow.setAnswer(true);
        Stage window = (Stage) btnYes.getScene().getWindow();
        window.close();
    }

    @FXML
    private void btnPressedNo(ActionEvent actionEvent) {
        ConfirmWindow.setAnswer(false);
        Stage window = (Stage) btnNo.getScene().getWindow();
        window.close();
    }
}
