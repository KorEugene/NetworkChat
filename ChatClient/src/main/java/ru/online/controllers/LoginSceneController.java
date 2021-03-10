package ru.online.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.online.AlertWindow;
import ru.online.LoginWindow;
import ru.online.MainWindow;

import java.io.IOException;

public class LoginSceneController {

    @FXML
    private TextField labLogin;
    @FXML
    private TextField labPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnCancel;

    @FXML
    private void btnLogin(ActionEvent actionEvent) throws IOException {
        // some logic for check credentials in DB
        if (labLogin.getLength() > 0) {
            MainWindow.displayMainWindow(labLogin.getText());
            LoginWindow.getLoginWindow().close();
        } else {
            AlertWindow.display();
        }
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        LoginWindow.closeProgram();
    }
}
