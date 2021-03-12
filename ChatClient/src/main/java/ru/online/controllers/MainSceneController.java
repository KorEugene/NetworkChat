package ru.online.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import ru.online.ClientApp;
import ru.online.LoginWindow;
import ru.online.MainWindow;
import ru.online.SettingsWindow;
import ru.online.messages.MessageDTO;
import ru.online.messages.MessageType;
import ru.online.network.ChatMessageService;
import ru.online.network.MessageProcessor;
import ru.online.network.MessageService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable, MessageProcessor {

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
        messageService = new ChatMessageService("localhost", 65500, this);
        onlineUsers.setItems(FXCollections.observableArrayList("Vasya", "Petya", "Kolya"));
    }

    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
        messageService.sendMessage("/exit");
        MainWindow.getMainWindow().close();
        MainWindow.getMainWindow().close();
        LoginWindow.displayLoginWindow(ClientApp.getMainStage());
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
            sendMessage();
            keyEvent.consume();
        }
    }

    @FXML
    private void btnSend(ActionEvent actionEvent) {
        sendMessage();
    }

    private void sendMessage() {
        String message = input.getText().trim();
        if (message.length() > 0) {
            MessageDTO dto = new MessageDTO();
            dto.setMessageType(checkMessageType(message));
            if (dto.getMessageType().equals(MessageType.PRIVATE_MESSAGE)) {
                dto.setTo((message.split(" ", 3))[1]);
                dto.setBody((message.split(" ", 3))[2]);
            } else {
                dto.setBody(message);
            }
            System.out.println(dto.toString());
            messageService.sendMessage(dto.convertToJson());
            input.clear();
        }
    }

    private MessageType checkMessageType(String message) {
        if (message.startsWith("/w")) {
            return MessageType.PRIVATE_MESSAGE;
        }
        return MessageType.PUBLIC_MESSAGE;
    }

    private void showMessage(String message) {
        chatArea.appendText(message + System.lineSeparator());
    }

    @Override
    public void processMessage(String msg) {
        Platform.runLater(() -> {
            MessageDTO dto = MessageDTO.convertFromJson(msg);
            System.out.println("Received message: " + dto.toString());
            switch (dto.getMessageType()) {
                case PUBLIC_MESSAGE -> showMessage(dto.getFrom() + ": " + dto.getBody());
                case PRIVATE_MESSAGE -> {
                    if (dto.getFrom().equals(MainWindow.getUserLogin())) {
                        showMessage(String.format("[private to: %s] ", dto.getTo()) + dto.getBody());
                    } else {
                        showMessage(String.format("[private from: %s] ", dto.getFrom()) + dto.getBody());
                    }
                }
                case AUTH_CONFIRM -> {
                    try {
                        MainWindow.displayMainWindow(dto.getBody());
                        LoginWindow.getLoginWindow().close();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                case ERROR_MESSAGE -> showError(dto);
            }
        });
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(e.getMessage());
        VBox dialog = new VBox();
        javafx.scene.control.Label label = new Label("Trace:");
        TextArea textArea = new TextArea();
        //TODO
        textArea.setText(e.getStackTrace()[0].toString());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }

    private void showError(MessageDTO dto) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong!");
        alert.setHeaderText(dto.getMessageType().toString());
        VBox dialog = new VBox();
        Label label = new Label("Trace:");
        TextArea textArea = new TextArea();
        //TODO
        textArea.setText(dto.getBody());
        dialog.getChildren().addAll(label, textArea);
        alert.getDialogPane().setContent(dialog);
        alert.showAndWait();
    }

    public MessageService getMessageService() {
        return messageService;
    }
}
