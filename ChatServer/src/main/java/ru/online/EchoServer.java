package ru.online;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

    private static Socket socket;

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(65500)) {
            System.out.println("Server started!");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client connected!");
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String message = in.readUTF();
                    if (message.equals("/exit")) {
                        out.writeUTF("/exit");
                        System.out.println("Client disconnected!");
                        break;
                    }
                    System.out.println("Received " + message);
                    out.writeUTF("Echo: " + message);
                }
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
