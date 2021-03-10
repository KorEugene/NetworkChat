package ru.online;

import ru.online.utility.Utility;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class SettingsWindow {

    private static final String FXML = "/SettingsScene.fxml";
    private static final String SETTINGS_TITLE = "Settings";
    private static final String PATH_TO_SETTINGS_ICON = "/img/settings_icon.png";
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 180;

    public static void display() throws IOException {

        Stage settingsWindow = new Stage();
        settingsWindow.initModality(Modality.APPLICATION_MODAL);
        settingsWindow.setTitle(SETTINGS_TITLE);

        InputStream chatIconStream = ClientApp.class.getResourceAsStream(PATH_TO_SETTINGS_ICON);
        Image chatIcon = new Image(chatIconStream);
        settingsWindow.getIcons().add(chatIcon);

        FXMLLoader loader = new FXMLLoader(SettingsWindow.class.getResource(FXML));
        Scene settingsScene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        Utility.centerStage(settingsWindow, WINDOW_WIDTH, WINDOW_HEIGHT);
        settingsWindow.setScene(settingsScene);
        settingsWindow.setResizable(false);
        settingsWindow.showAndWait();
    }
}
