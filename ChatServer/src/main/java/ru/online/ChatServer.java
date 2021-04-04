package ru.online;

import ru.online.auth.AuthService;
import ru.online.auth.PrimitiveAuthService;
import ru.online.auth.SQLiteAuthService;
import ru.online.messages.MessageDTO;
import ru.online.messages.MessageType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServer {

    private static final int PORT_NUMBER = 65500;

    private List<ClientHandler> onlineClientsList;
    private AuthService authService;
    private ExecutorService executorService;

    public ChatServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
            System.out.println("Server started");
//            authService = new PrimitiveAuthService();
            authService = new SQLiteAuthService();
            authService.start();
            onlineClientsList = new LinkedList<>();
            executorService = Executors.newCachedThreadPool();
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

    public synchronized void sendPrivateMessage(MessageDTO dto) {
        for (ClientHandler clientHandler : onlineClientsList) {
            if (clientHandler.getCurrentUserName().equals(dto.getTo())) {
                clientHandler.sendMessage(dto);
                break;
            }
        }
    }

    public synchronized void broadcastOnlineClients() {
        MessageDTO dto = new MessageDTO();
        dto.setMessageType(MessageType.CLIENTS_LIST_MESSAGE);
        List<String> onlineClients = new LinkedList<>();
        for (ClientHandler clientHandler : onlineClientsList) {
            onlineClients.add(clientHandler.getCurrentUserName());
        }
        dto.setUsersOnline(onlineClients);
        broadcastMessage(dto);
    }

    public synchronized boolean isUserBusy(String username) {
        for (ClientHandler clientHandler : onlineClientsList) {
            if (clientHandler.getCurrentUserName().equals(username)) return true;
        }
        return false;
    }

    public synchronized void broadcastMessage(MessageDTO dto) {
        for (ClientHandler clientHandler : onlineClientsList) {
            clientHandler.sendMessage(dto);
        }
    }

    public synchronized boolean usernameIsExist(String username) {
        return authService.checkUsername(username);
    }

    public synchronized void updateUsername(String currentUsername, String newUsername) {
        authService.updateUsername(currentUsername, newUsername);
    }

    public synchronized void subscribe(ClientHandler c) {
        onlineClientsList.add(c);
        broadcastOnlineClients();
    }

    public synchronized void unsubscribe(ClientHandler c) {
        onlineClientsList.remove(c);
        broadcastOnlineClients();
    }

    public AuthService getAuthService() {
        return authService;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
