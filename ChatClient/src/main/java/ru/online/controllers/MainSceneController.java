package ru.online.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.online.LoginWindow;
import ru.online.MainWindow;
import ru.online.SettingsWindow;
import ru.online.utility.MessageService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

    private static final String URI_JAVAFX = "https://openjfx.io/";

    @FXML
    private TextArea chatArea;
    @FXML
    private ListView onlineUsers;
    @FXML
    private Button btnSendMessage;
    @FXML
    private TextArea input;

    private MessageService messageService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageService = new MessageService(chatArea);
        messageService.connectToServer();
        onlineUsers.setItems(FXCollections.observableArrayList("Vasya", "Petya", "Kolya"));
    }

    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        messageService.sendMessage("/exit");
        MainWindow.getMainWindow().close();
        LoginWindow.displayLoginWindow(LoginWindow.getLoginWindow());
    }

    @FXML
    private void exit(ActionEvent actionEvent) {
        MainWindow.closeProgram();
    }

    @FXML
    private void aboutJavaFX(ActionEvent actionEvent) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(new URI(URI_JAVAFX));
    }

    @FXML
    private void getSettings(ActionEvent actionEvent) throws IOException {
        SettingsWindow.display();
    }

    @FXML
    private void pressEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && keyEvent.isShiftDown()) {
            input.insertText(input.getCaretPosition(), "\n");
        } else if (keyEvent.getCode() == KeyCode.ENTER) {
            appendTextFromInput();
            keyEvent.consume();
        }
    }

    @FXML
    private void btnSend(ActionEvent actionEvent) {
        appendTextFromInput();
    }

    private void appendTextFromInput() {
        String message = input.getText();
        if (message.length() > 0) {
            messageService.sendMessage(message);
            input.clear();
        }
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
