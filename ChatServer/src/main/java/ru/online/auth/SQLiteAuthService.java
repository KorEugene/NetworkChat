package ru.online.auth;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteAuthService implements AuthService {

    private final String GET_USERNAME_BY_LOGIN_PASS_QUERY = "SELECT username FROM clients WHERE login = ? AND password = ?;";
    private final String UPDATE_USERNAME_QUERY = "UPDATE clients SET username = ? WHERE username = ?;";
    private final String CHECK_USERNAME_QUERY = "SELECT username FROM clients WHERE username = ?;";
    private final String GET_CLIENTS_QUERY = "SELECT * FROM clients;";
    private final String DROP_CLIENTS_TABLE_QUERY = "DROP TABLE if EXISTS clients;";
    private final String CREATE_CLIENTS_TABLE_QUERY = "CREATE TABLE if NOT EXISTS clients (id INTEGER PRIMARY KEY autoincrement, username TEXT, login TEXT, password TEXT);";
    private final String INSERT_DEFAULT_CLIENT_1_QUERY = "INSERT INTO clients (username, login, password) VALUES ('user1', 'log1', 'pass1');";
    private final String INSERT_DEFAULT_CLIENT_2_QUERY = "INSERT INTO clients (username, login, password) VALUES ('user2', 'log2', 'pass2');";
    private final String INSERT_DEFAULT_CLIENT_3_QUERY = "INSERT INTO clients (username, login, password) VALUES ('user3', 'log3', 'pass3');";

    private List<Client> clients;
    private Connection connection;
    private Statement statement;
    private PreparedStatement ps;

    public SQLiteAuthService() {
        clients = new ArrayList<>();
    }

    @Override
    public void start() {
        System.out.println("Connecting to DB.");
        connect();
        create();
        getClients();
        System.out.println("Connected to DB. Auth started.");
    }

    @Override
    public void stop() {
        System.out.println("Disconnecting from DB.");
        disconnect();
        System.out.println("Disconnected from DB. Auth stopped.");
    }

    @Override
    public String getUsernameByLoginPass(String login, String pass) {
        String username = null;
        try {
            ps = connection.prepareStatement(GET_USERNAME_BY_LOGIN_PASS_QUERY);
            ps.setString(1, login);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return username;
    }

    @Override
    public void updateUsername(String currentUsername, String newUsername) {
        try {
            ps = connection.prepareStatement(UPDATE_USERNAME_QUERY);
            ps.setString(1, newUsername);
            ps.setString(2, currentUsername);
            ps.executeUpdate();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public boolean checkUsername(String newUsername) {
        try {
            ps = connection.prepareStatement(CHECK_USERNAME_QUERY);
            ps.setString(1, newUsername);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        return false;
    }

    private void getClients() {
        try (ResultSet rs = statement.executeQuery(GET_CLIENTS_QUERY)) {
            while (rs.next()) {
                clients.add(new Client(rs.getString("username"), rs.getString("login"), rs.getString("password")));
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void create() {
        try {
            statement.executeUpdate(DROP_CLIENTS_TABLE_QUERY);
            statement.executeUpdate(CREATE_CLIENTS_TABLE_QUERY);
            insertDefault();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void insertDefault() throws SQLException {
        statement.executeUpdate(INSERT_DEFAULT_CLIENT_1_QUERY);
        statement.executeUpdate(INSERT_DEFAULT_CLIENT_2_QUERY);
        statement.executeUpdate(INSERT_DEFAULT_CLIENT_3_QUERY);
    }

    private void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:clients.db");
            statement = connection.createStatement();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void disconnect() {
        try {
            if (statement != null) statement.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
        try {
            if (connection != null) connection.close();
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
