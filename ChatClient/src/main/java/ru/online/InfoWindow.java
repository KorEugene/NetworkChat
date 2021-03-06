package ru.online;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.online.utility.Utility;

import java.io.IOException;
import java.io.InputStream;

public class InfoWindow {

    private static final String FXML = "/InfoScene.fxml";
    private static final String PATH_TO_INFO_ICON = "/img/info_icon.png";
    private static final String INFO_TITLE = "Info";
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 120;

    private static Stage infoWindow;
    private static FXMLLoader infoLoader;

    public static void init() throws IOException {

        infoWindow = new Stage();
        infoWindow.initModality(Modality.APPLICATION_MODAL);
        infoWindow.setTitle(INFO_TITLE);

        InputStream infoIconStream = ClientApp.class.getResourceAsStream(PATH_TO_INFO_ICON);
        Image infoIcon = new Image(infoIconStream);
        infoWindow.getIcons().add(infoIcon);

        infoLoader = new FXMLLoader(InfoWindow.class.getResource(FXML));
        Scene infoScene = new Scene(infoLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        Utility.centerStage(infoWindow, WINDOW_WIDTH, WINDOW_HEIGHT);
        infoWindow.setScene(infoScene);
        infoWindow.setResizable(false);
    }

    public static void display() {
        infoWindow.showAndWait();
    }

    public static FXMLLoader getInfoLoader() {
        return infoLoader;
    }
}
