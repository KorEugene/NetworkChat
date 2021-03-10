package ru.online;

import javafx.application.Application;
import javafx.stage.Stage;

import java.awt.*;

public class ClientApp extends Application {

    private static final GraphicsDevice DEFAULT_SCREEN_DEVICE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private static final int DEFAULT_SCREEN_WIDTH = DEFAULT_SCREEN_DEVICE.getDisplayMode().getWidth();
    private static final int DEFAULT_SCREEN_HEIGHT = DEFAULT_SCREEN_DEVICE.getDisplayMode().getHeight();

    private static Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        LoginWindow.displayLoginWindow(mainStage);
    }

    public static int getDefaultScreenWidth() {
        return DEFAULT_SCREEN_WIDTH;
    }

    public static int getDefaultScreenHeight() {
        return DEFAULT_SCREEN_HEIGHT;
    }
}
