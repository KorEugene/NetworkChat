package ru.online;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;

    private boolean connectionIsAlive = true;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() throws IOException {
        while (connectionIsAlive) {
            try {
                String clientMessage = inputStream.readUTF();
                if (clientMessage.startsWith("/")) {
                    if (clientMessage.startsWith("/exit")) {
                        outputStream.writeUTF("/exit");
                        System.out.println("Client disconnected!");
                        connectionIsAlive = false;
                        break;
                    }
                }
                System.out.println("Got message: " + clientMessage);
                outputStream.writeUTF("Client: " + clientMessage);
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }
    }

    private void writeMessages() {
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (connectionIsAlive) {
                    String serverMessage = scanner.nextLine();
                    if (serverMessage.length() > 0) {
                        System.out.println("Send message: " + serverMessage);
                        outputStream.writeUTF("Server: " + serverMessage);
                    }
                }
            } catch (IOException exception) {
                System.out.println(exception.getMessage());
            }
        }).start();
    }

    @Override
    public void run() {
        try {
            writeMessages();
            readMessages();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
