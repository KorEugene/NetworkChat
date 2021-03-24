package ru.online;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.online.utility.Utility;

import java.io.IOException;
import java.io.InputStream;

public class AlertWindow {

    private static final String FXML = "/AlertScene.fxml";
    private static final String PATH_TO_ALERT_ICON = "/img/alert_icon.png";
    private static final String ALERT_TITLE = "Warning!";
    private static final int WINDOW_WIDTH = 200;
    private static final int WINDOW_HEIGHT = 120;

    public static void display() throws IOException {

        Stage alertWindow = new Stage();
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(ALERT_TITLE);

        InputStream alertIconStream = ClientApp.class.getResourceAsStream(PATH_TO_ALERT_ICON);
        Image alertIcon = new Image(alertIconStream);
        alertWindow.getIcons().add(alertIcon);

        FXMLLoader loader = new FXMLLoader(AlertWindow.class.getResource(FXML));
        Scene alertScene = new Scene(loader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);

        Utility.centerStage(alertWindow, WINDOW_WIDTH, WINDOW_HEIGHT);
        alertWindow.setScene(alertScene);
        alertWindow.setResizable(false);
        alertWindow.showAndWait();
    }
}
