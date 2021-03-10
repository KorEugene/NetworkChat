package ru.online.utility;

import javafx.scene.control.TextArea;

import java.io.IOException;

public class MessageService {

    private static final String HOST = "localhost";
    private static final int PORT = 65500;
    private NetworkHelper networkHelper;
    private TextArea chatArea;

    public MessageService(TextArea chatArea) {
        this.chatArea = chatArea;
    }

    public void connectToServer() {
        try {
            networkHelper = new NetworkHelper();
            networkHelper.openConnection(HOST, PORT, this);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void sendMessage(String message) {
        networkHelper.writeMessage(message);
    }

    public void receiveMessage(String message) {
        chatArea.appendText(message + System.lineSeparator());
    }
}
