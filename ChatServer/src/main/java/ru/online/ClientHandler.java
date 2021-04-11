package ru.online;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.online.auth.SQLiteAuthService;
import ru.online.messages.MessageDTO;
import ru.online.messages.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientHandler {

    public static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private ChatServer chatServer;
    private String currentUserName;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            LOGGER.info("Client Handler created!");

            chatServer.getExecutorService().execute(() -> {
                LOGGER.info("Client connection execute in: " + Thread.currentThread().getName());
                authenticate();
                readMessages();
            });

        } catch (IOException exception) {
            LOGGER.error(exception);
        }
    }

    public void sendMessage(MessageDTO dto) {
        try {
            outputStream.writeUTF(dto.convertToJson());
        } catch (IOException exception) {
            LOGGER.error(exception);
        }
    }

    private void readMessages() {
        try {
            while (!Thread.currentThread().isInterrupted() || socket.isConnected()) {
                String msg = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(msg);
                dto.setFrom(currentUserName);

                switch (dto.getMessageType()) {
                    case PUBLIC_MESSAGE -> chatServer.broadcastMessage(dto);
                    case PRIVATE_MESSAGE -> {
                        chatServer.sendPrivateMessage(dto);
                        this.sendMessage(dto);
                    }
                    case SETTINGS_USERNAME_MESSAGE -> {
                        MessageDTO response = new MessageDTO();
                        String newUsername = dto.getBody();
                        if (chatServer.usernameIsExist(newUsername)) {
                            response.setMessageType(MessageType.ERROR_MESSAGE);
                            response.setBody("Username is busy!");
                            LOGGER.warn(newUsername + " username is busy");
                        } else {
                            chatServer.updateUsername(currentUserName, newUsername);
                            response.setMessageType(MessageType.SETTINGS_USERNAME_CONFIRM);
                            LOGGER.info("Username: " + currentUserName + " was changed: " + newUsername);
                        }
                        sendMessage(response);
                    }
                }
            }
        } catch (IOException exception) {
            LOGGER.error(exception);
        } finally {
            closeHandler();
        }
    }

    private void authenticate() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        LOGGER.info("Authenticate started!");
        Future<Boolean> task = executorService.submit(() -> {
            while (true) {
                String authMessage = inputStream.readUTF();
                LOGGER.info("received msg ");
                MessageDTO dto = MessageDTO.convertFromJson(authMessage);
                String username = chatServer.getAuthService().getUsernameByLoginPass(dto.getLogin(), dto.getPassword());
                MessageDTO response = new MessageDTO();
                if (username == null) {
                    response.setMessageType(MessageType.ERROR_MESSAGE);
                    response.setBody("Wrong login or pass!");
                    LOGGER.warn("Wrong auth");
                    sendMessage(response);
                } else if (chatServer.isUserBusy(username)) {
                    response.setMessageType(MessageType.ERROR_MESSAGE);
                    response.setBody("You are clone!!!");
                    LOGGER.warn("Clone");
                    sendMessage(response);
                } else {
                    response.setMessageType(MessageType.AUTH_CONFIRM);
                    response.setBody(username);
                    response.setLogin(dto.getLogin());
                    currentUserName = username;
                    chatServer.subscribe(this);
                    LOGGER.info("Client subscribed");
                    sendMessage(response);
                    return true;
                }
            }
        });
        try {
            task.get(120, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException exception) {
            LOGGER.warn("Client timed out!");
            closeHandler();
        }
    }

    public void closeHandler() {
        try {
            chatServer.unsubscribe(this);
            socket.close();
        } catch (IOException exception) {
            LOGGER.error(exception);
        }
    }

    public String getCurrentUserName() {
        return currentUserName;
    }
}
