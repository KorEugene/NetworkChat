package ru.online.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.online.MainWindow;
import ru.online.messages.MessageDTO;
import ru.online.messages.MessageType;
import ru.online.network.MessageService;

public class SettingsSceneController {

    @FXML
    private TextField tFIPAddress;
    @FXML
    private TextField tFIncomingPort;
    @FXML
    private TextField tFOutgoingPort;
    @FXML
    private Button btnApplySettings;
    @FXML
    private Button btnCancelSettings;
    @FXML
    private TextField tFUsername;

    private FXMLLoader mainLoader = MainWindow.getMainLoader();
    private MainSceneController mainSceneController = mainLoader.getController();
    private MessageService messageService = mainSceneController.getMessageService();

    @FXML
    private void applyButtonAction(ActionEvent actionEvent) {
        String newUsername = tFUsername.getText();
        if (newUsername.length() > 0) {
            newUsername = newUsername.replaceAll("\\s+", "");
            MessageDTO dto = new MessageDTO();
            dto.setMessageType(MessageType.SETTINGS_USERNAME_MESSAGE);
            dto.setBody(newUsername);
            messageService.sendMessage(dto.convertToJson());
            System.out.println("Request has been sent to change the username.");
        } else {
            Stage window = (Stage) btnApplySettings.getScene().getWindow();
            window.close();
        }
    }

    @FXML
    private void closeButtonAction(ActionEvent actionEvent) {
        Stage window = (Stage) btnCancelSettings.getScene().getWindow();
        window.close();
    }
}
