package ru.online.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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

    private final String ALL = "SEND TO ALL";

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
    }

    @FXML
    private void logout(ActionEvent actionEvent) throws IOException {
//        messageService.sendMessage("/exit");
//        MainWindow.getMainWindow().close();
        MainWindow.getMainWindow().close();
        messageService.disconnectFromServer();
        messageService = new ChatMessageService("localhost", 65500, this);
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

    private void refreshUserList(MessageDTO dto) {
        dto.getUsersOnline().add(0, ALL);
        onlineUsers.setItems(FXCollections.observableArrayList(dto.getUsersOnline()));
        onlineUsers.getSelectionModel().selectFirst();
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
        String msg = input.getText().trim();
        if (msg.length() == 0) return;

        MessageDTO dto = new MessageDTO();
        String selected = (String) onlineUsers.getSelectionModel().getSelectedItem();
        if (selected.equals(ALL)) dto.setMessageType(MessageType.PUBLIC_MESSAGE);
        else {
            dto.setMessageType(MessageType.PRIVATE_MESSAGE);
            dto.setTo(selected);
        }

        dto.setBody(msg);
        messageService.sendMessage(dto.convertToJson());
        input.clear();

    }

    private void showMessage(MessageDTO message) {
        String msg = String.format("[%s] [%s] -> %s%n", message.getMessageType(), message.getFrom(), message.getBody());
        chatArea.appendText(msg);
    }

    @Override
    public void processMessage(String msg) {
        Platform.runLater(() -> {
            MessageDTO dto = MessageDTO.convertFromJson(msg);
            System.out.println("Received message: ");
            switch (dto.getMessageType()) {
                case PUBLIC_MESSAGE, PRIVATE_MESSAGE -> showMessage(dto);
                case CLIENTS_LIST_MESSAGE -> refreshUserList(dto);
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
