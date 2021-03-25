package ru.online.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.online.*;
import ru.online.messages.MessageDTO;
import ru.online.messages.MessageType;
import ru.online.network.MessageService;

import java.io.IOException;

public class LoginSceneController {

    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passField;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnCancel;

    private FXMLLoader mainLoader = MainWindow.getMainLoader();
    private MainSceneController mainSceneController = mainLoader.getController();
    private MessageService messageService = mainSceneController.getMessageService();
    private InfoSceneController infoSceneController;

    @FXML
    private void btnLogin(ActionEvent actionEvent) {
        String log = loginField.getText();
        String pass = passField.getText();
        if (log.equals("") || pass.equals("")) {
            try {
                InfoWindow.init();
                infoSceneController = InfoWindow.getInfoLoader().getController();
                infoSceneController.getLabelMessage().setText("Empty login/password is not allowed!");
                InfoWindow.display();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return;
        }
        MessageDTO dto = new MessageDTO();
        dto.setLogin(log);
        dto.setPassword(pass);
        dto.setMessageType(MessageType.SEND_AUTH_MESSAGE);
        messageService.sendMessage(dto.convertToJson());
        System.out.println("Sent credentials.");
    }

    @FXML
    private void btnCancel(ActionEvent actionEvent) {
        LoginWindow.closeProgram();
    }
}
