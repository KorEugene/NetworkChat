package ru.online.utility;

import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class NetworkHelper {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public void openConnection(String serverAddress, int serverPort, MessageService messageService) throws IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        Thread connectionThread;
        connectionThread = new Thread(() -> {
            while (true) {
                try {
                    String message = inputStream.readUTF();
                    if (message.equalsIgnoreCase("/exit")) {
                        closeConnection();
                        break;
                    }
                    Platform.runLater(() -> messageService.receiveMessage(message));
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
        });
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void writeMessage(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
