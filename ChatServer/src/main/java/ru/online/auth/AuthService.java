package ru.online.auth;

public interface AuthService {

    void start();

    void stop();

    String getUsernameByLoginPass(String login, String pass);

    boolean checkUsername(String newUsername);

    void updateUsername(String currentUsername, String newUsername);
}
