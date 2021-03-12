package ru.online;

import ru.online.messages.MessageDTO;
import ru.online.messages.MessageType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {

    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private ChatServer chatServer;
    private String user;

    public ClientHandler(Socket socket, ChatServer chatServer) {
        try {
            this.chatServer = chatServer;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            System.out.println("CH created!");
            new Thread(() -> {
                authenticate();
                readMessages();
            }).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(MessageDTO dto) {
        try {
            outputStream.writeUTF(dto.convertToJson());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessages() {
        try {
            while (true) {
                String msg = inputStream.readUTF();
                MessageDTO dto = MessageDTO.convertFromJson(msg);
                dto.setFrom(user);

                switch (dto.getMessageType()) {
//                    case SEND_AUTH_MESSAGE -> authenticate(dto);
                    case PUBLIC_MESSAGE -> chatServer.broadcastMessage(dto);
                    case PRIVATE_MESSAGE -> {
                        ClientHandler addressee = chatServer.isSubscribed(dto.getTo());
                        if (addressee != null) {
                            chatServer.sendPrivateMessage(addressee, dto);
                            chatServer.sendPrivateMessage(this, dto);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void authenticate() {
        System.out.println("Authenticate started!");
        try {
            while (true) {
                String authMessage = inputStream.readUTF();
                System.out.println("received msg ");
                MessageDTO dto = MessageDTO.convertFromJson(authMessage);
                String username = chatServer.getAuthService().getUsernameByLoginPass(dto.getLogin(), dto.getPassword());
                MessageDTO send = new MessageDTO();
                if (username == null) {
                    send.setMessageType(MessageType.ERROR_MESSAGE);
                    send.setBody("Wrong login or pass!");
                    System.out.println("Wrong auth");
                    sendMessage(send);
                } else {
                    send.setMessageType(MessageType.AUTH_CONFIRM);
                    send.setBody(username);
                    user = username;
                    chatServer.subscribe(this);
                    System.out.println("Subscribed");
                    sendMessage(send);
                    break;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeHandler() {
        try {
            chatServer.unsubscribe(this);
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUser() {
        return user;
    }
}
