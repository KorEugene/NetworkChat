package ru.online;

import ru.online.auth.AuthService;
import ru.online.auth.PrimitiveAuthService;
import ru.online.messages.MessageDTO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {

    private static final int PORT_NUMBER = 65500;

    private List<ClientHandler> clientHandlers;
    private AuthService authService;

    public ChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started");
            authService = new PrimitiveAuthService();
            authService.start();
            clientHandlers = new LinkedList<>();
            while (true) {
                System.out.println("Waiting for connection...");
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");
                new ClientHandler(socket, this);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public synchronized void broadcastMessage(MessageDTO dto) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.sendMessage(dto);
        }
    }

    public synchronized ClientHandler isSubscribed(String username) {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getUser().equals(username)) return clientHandler;
        }
        return null;
    }

    public void sendPrivateMessage(ClientHandler addressee, MessageDTO dto) {
        addressee.sendMessage(dto);
    }

    public synchronized void subscribe(ClientHandler c) {
        clientHandlers.add(c);
    }

    public synchronized void unsubscribe(ClientHandler c) {
        clientHandlers.remove(c);
    }

    public AuthService getAuthService() {
        return authService;
    }
}
