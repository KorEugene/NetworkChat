package ru.online;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

    private static final int PORT_NUMBER = 65500;

    public ChatServer() {

        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started");
            while (true) {
                System.out.println("Waiting for new connections...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                new ClientHandler(socket).run();
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
